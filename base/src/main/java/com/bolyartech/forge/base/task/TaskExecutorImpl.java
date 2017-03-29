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
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;


/**
 * Executes tasks that produce result of type <code>T</code>
 * <p>
 * Task execution is time limited, when the task does not finish in the time budget it is seen as failed
 *
 * @param <T> Type of the produced result
 */
@SuppressWarnings("WeakerAccess")
public class TaskExecutorImpl<T> implements TaskExecutor<T> {
    public static final int DEFAULT_TTL_CHECK_INTERVAL = 1000;
    public static final int DEFAULT_TASK_TTL = 5000;
    private static final int DEFAULT_EXECUTOR_SERVICE_THREADS = 2;


    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(this.getClass());
    private final AtomicLong mSequenceGenerator = new AtomicLong(0);
    private final List<Listener<T>> mListeners = new CopyOnWriteArrayList<>();
    private final Map<Long, InFlightTtlHelper<?>> mTasksInFlight = new ConcurrentHashMap<>();
    private final ListeningExecutorService mTaskExecutorService;
    private final int mTtlCheckInterval;
    private final int mTaskTtl;
    private final TimeProvider mTimeProvider;
    private final ScheduledExecutorService mScheduler;
    private volatile boolean mIsStarted = false;
    private volatile boolean mIsShutdown = false;
    private ScheduledFuture<?> mTtlChecker;


    /**
     * Creates new TaskExecutorImpl with default settings
     */
    @SuppressWarnings("unused")
    @Inject
    public TaskExecutorImpl() {
        this(createDefaultExecutorService(),
                DEFAULT_TTL_CHECK_INTERVAL,
                DEFAULT_TASK_TTL,
                createDefaultScheduler(),
                new TimeProviderImpl()
        );
    }


    /**
     * Creates new TaskExecutorImpl with default TTL
     * @param taskExecutorService Task executor service to be used
     * @param scheduler Scheduled executor to be used
     */
    @SuppressWarnings("unused")
    public TaskExecutorImpl(ExecutorService taskExecutorService, ScheduledExecutorService scheduler) {
        this(taskExecutorService, DEFAULT_TTL_CHECK_INTERVAL, DEFAULT_TASK_TTL, scheduler, new TimeProviderImpl());
    }


    /**
     * Creates new TaskExecutorImpl with default TTL
     * @param taskExecutorService Task executor service to be used
     * @param ttlCheckInterval Interval to check for TTLed tasks
     * @param taskTtl TTL - how long to wait for the tasks by default before they are seen as failed and cancelled
     * @param scheduler Scheduled executor to be used
     * @param timeProvider Time provider
     */
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


    public static ExecutorService createDefaultExecutorService() {
        return Executors.newFixedThreadPool(DEFAULT_EXECUTOR_SERVICE_THREADS);
    }


    static boolean isTtled(InFlightTtlHelper<?> hlp, TimeProvider timeProvider) {
        // - 1 is used in order unit tests with 0 TTL to be able to work consistently
        return hlp.mStartedAt + hlp.mTtl - 1 < timeProvider.getVmTime();
    }


    public static ScheduledExecutorService createDefaultScheduler() {
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
                mTasksInFlight.put(taskId, new InFlightTtlHelper<>(taskId, mTimeProvider.getVmTime(), ttl, lf));

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


    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        if (!mIsShutdown) {
            mLogger.warn("Seems you forgot to shutdown TaskExecutorImpl " + this);
            shutdown();
        }
    }


    protected void onIdle() {
        // empty
    }


    @SuppressWarnings("unused")
    protected int getTasksInFlightCount() {
        return mTasksInFlight.size();
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
}
