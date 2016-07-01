package com.bolyartech.forge.base.task;

import com.bolyartech.forge.base.misc.TimeProvider;
import com.bolyartech.forge.base.misc.TimeProviderImpl;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;


public class TaskExecutorImpl<T> implements TaskExecutor<T> {
    private static final int TTL_CHECK_INTERVAL = 1000;
    private static final int DEFAULT_TASK_TTL = 5000;
    private static final int DEFAULT_EXECUTOR_SERVICE_THREADS = 2;


    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final AtomicLong mSequenceGenerator = new AtomicLong(0);
    private final List<Listener<T>> mListeners = new CopyOnWriteArrayList<>();
    private final Map<Long, InFlightTtlHelper<?>> mTasksInFlight = new ConcurrentHashMap<>();
    private volatile boolean mIsStarted = false;
    private volatile boolean mIsShutdown = false;

    private final ListeningExecutorService mTaskExecutorService;
    private final int mTtlCheckInterval;
    private final int mTaskTtl;
    private final TimeProvider mTimeProvider;

    private ScheduledFuture<?> mTtlChecker;


    private final ScheduledExecutorService mScheduler;


    @Inject
    public TaskExecutorImpl() {
        this(createDefaultExecutorService(),
                TTL_CHECK_INTERVAL,
                DEFAULT_TASK_TTL,
                createDefaultScheduler(),
                new TimeProviderImpl()
        );
    }


    private static ScheduledExecutorService createDefaultScheduler() {
        return Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @SuppressWarnings("NullableProblems")
            @Override
            public Thread newThread(Runnable r) {
                Thread ret = new Thread(r);
                ret.setName("TaskExecutorImpl Scheduler");
                return ret;
            }
        });
    }


    @SuppressWarnings("unused")
    public TaskExecutorImpl(ExecutorService taskExecutorService, ScheduledExecutorService scheduler) {
        this(taskExecutorService, TTL_CHECK_INTERVAL, DEFAULT_TASK_TTL, scheduler, new TimeProviderImpl());
    }


    public TaskExecutorImpl(ExecutorService taskExecutorService,
                            int ttlCheckInterval,
                            int taskTtl,
                            ScheduledExecutorService scheduler,
                            TimeProvider timeProvider) {


        if (taskExecutorService == null) {
            throw new NullPointerException("Parameter 'exchangeExecutorService' is null");
        }

        if (scheduler == null) {
            throw new NullPointerException("Parameter 'scheduler' is null");
        }

        if (timeProvider == null) {
            throw new NullPointerException("Parameter 'timeProvider' is null");
        }

        if (ttlCheckInterval <= 0) {
            throw new IllegalArgumentException("ttlCheckInterval is <= 0: " + ttlCheckInterval);
        }

        if (taskTtl <= 0) {
            throw new IllegalArgumentException("exchangeTtl is <= 0: " + ttlCheckInterval);
        }

        mTaskExecutorService = MoreExecutors.listeningDecorator(taskExecutorService);
        mTtlCheckInterval = ttlCheckInterval;
        mTaskTtl = taskTtl;
        mScheduler = scheduler;
        mTimeProvider = timeProvider;
    }


    @Override
    public void start() {
        mLogger.trace("ExchangeFunctionalityImpl {} started", this);
        mTtlChecker = mScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                checkAndRemoveTtled();
            }
        }, mTtlCheckInterval, mTtlCheckInterval, TimeUnit.MILLISECONDS);
        mIsStarted = true;
    }


    @Override
    public void shutdown() {
        if (mTtlChecker != null) {
            mTtlChecker.cancel(true);
        }

        mScheduler.shutdown();

        mTaskExecutorService.shutdown();
        mIsShutdown = true;
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        if (!mIsShutdown) {
            mLogger.warn("Seems you forgot to shutdown TaskExecutorImpl " + this);
            shutdown();
        }
    }


    @Override
    public boolean isStarted() {
        return mIsStarted;
    }


    @Override
    public boolean isShutdown() {
        return mIsShutdown;
    }


    @Override
    public void executeTask(Callable<T> task) {
        executeTask(task, generateTaskId(), mTaskTtl);
    }


    @Override
    public void executeTask(Callable<T> task, long taskId) {
        executeTask(task, taskId, mTaskTtl);
    }


    @Override
    public synchronized void executeTask(Callable<T> task, final long taskId, long ttl) {
        if (ttl < 0) {
            throw new IllegalArgumentException(MessageFormat.format("ttl is negative {0} < 0", ttl));
        }

        if (mIsStarted) {
            if (!mIsShutdown) {
                ListenableFuture<T> lf = mTaskExecutorService.submit(task);
                mTasksInFlight.put(taskId, new InFlightTtlHelper<>(taskId, mTimeProvider.getTime(), ttl, lf));

                //noinspection NullableProblems
                Futures.addCallback(lf, new FutureCallback<T>() {
                    @Override
                    public void onSuccess(T result) {
                        notifySuccess(taskId, result);
                    }


                    @Override
                    public void onFailure(Throwable t) {
                        if (!(t instanceof CancellationException)) {
                            // if cancelled we don't notify because the user himself cancelled the task
                            notifyFailure(taskId);
                        }
                    }
                });
            } else {
                throw new RejectedExecutionException("Already shutdown");
            }
        } else {
            throw new RejectedExecutionException("You forgot to call start() before this execute()");
        }
    }


    private void notifySuccess(long taskId, T result) {
        if (result != null) {
            for (Listener<T> l : mListeners) {
                l.onTaskSuccess(taskId, result);
            }
            removeTask(taskId);
        } else {
            notifyFailure(taskId);
        }
    }


    private void notifyFailure(long taskId) {
        for (Listener l : mListeners) {
            l.onTaskFailure(taskId);
        }

        removeTask(taskId);
    }


    private synchronized void removeTask(long taskId) {
        mTasksInFlight.remove(taskId);
        if (mTasksInFlight.size() == 0) {
            onIdle();
        }
    }


    @Override
    public synchronized void cancelTask(long taskId, boolean mayInterruptIfRunning) {
        InFlightTtlHelper<?> f = mTasksInFlight.remove(taskId);
        if (f != null) {
            f.mFuture.cancel(mayInterruptIfRunning);
        }
    }


    @Override
    public void addListener(Listener<T> listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }


    @Override
    public void removeListener(Listener<T> listener) {
        mListeners.remove(listener);
    }


    @Override
    public Long generateTaskId() {
        if (!mIsStarted) {
            mLogger.warn("You need to call start() before reserving id");
        }
        return mSequenceGenerator.incrementAndGet();
    }


    private static ExecutorService createDefaultExecutorService() {
        return Executors.newFixedThreadPool(DEFAULT_EXECUTOR_SERVICE_THREADS);
    }


    void checkAndRemoveTtled() {
        for (InFlightTtlHelper<?> hlp : mTasksInFlight.values()) {
            if (isTtled(hlp, mTimeProvider)) {
                mLogger.debug("Task {} TTLed", hlp.mTaskId);
                hlp.mFuture.cancel(true);
                notifyFailure(hlp.mTaskId);
            }
        }
    }


    static boolean isTtled(InFlightTtlHelper<?> hlp, TimeProvider timeProvider) {
        // - 1 is used in order unit tests with 0 TTL to be able to work consistently
        return hlp.mStartedAt + hlp.mTtl - 1 < timeProvider.getTime();
    }


    static class InFlightTtlHelper<T> {
        final long mTaskId;
        final long mStartedAt;
        final long mTtl;
        final ListenableFuture<T> mFuture;


        public InFlightTtlHelper(long taskId,
                                 long startedAt,
                                 long ttl,
                                 ListenableFuture<T> future
        ) {
            mTaskId = taskId;
            mStartedAt = startedAt;
            mTtl = ttl;
            mFuture = future;
        }
    }


    protected void onIdle() {
        // empty
    }


    protected int getTasksInFlightCount() {
        return mTasksInFlight.size();
    }
}
