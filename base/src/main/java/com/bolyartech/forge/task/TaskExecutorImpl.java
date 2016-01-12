package com.bolyartech.forge.task;

import com.bolyartech.forge.exchange.Exchange;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
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


/**
 * Created by ogre on 2016-01-12 12:38
 */
public class TaskExecutorImpl<T> implements TaskExecutor<T> {
    private static final int TTL_CHECK_INTERVAL = 1000;
    private static final int DEFAULT_TASK_TTL = 5000;
    private static final int DEFAULT_EXECUTOR_SERVICE_THREADS = 2;


    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final AtomicLong mSequenceGenerator = new AtomicLong(0);
    private final List<Listener<T>> mListeners = new CopyOnWriteArrayList<>();
    private final Map<Long, InFlightTtlHelper<T>> mTasksInFlight = new ConcurrentHashMap<>();
    private volatile boolean mIsStarted = false;
    private volatile boolean mIsShutdown = false;

    private final ListeningExecutorService mTaskExecutorService;
    private final int mTtlCheckInterval;
    private final int mTaskTtl;
    private ScheduledFuture<?> mTtlChecker;



    private final ScheduledExecutorService mScheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Thread newThread(Runnable r) {
            Thread ret = new Thread(r);
            ret.setName("TaskExecutorImpl Scheduler");
            return ret;
        }
    });


    public TaskExecutorImpl() {
        this(createDefaultExecutorService(),
                TTL_CHECK_INTERVAL,
                DEFAULT_TASK_TTL);
    }



    public TaskExecutorImpl(ExecutorService taskExecutorService) {
        this(taskExecutorService, TTL_CHECK_INTERVAL, DEFAULT_TASK_TTL);
    }



    public TaskExecutorImpl(ExecutorService taskExecutorService, int ttlCheckInterval, int taskTtl) {
        if (taskExecutorService == null) {
            throw new NullPointerException("Parameter 'exchangeExecutorService' is nul");
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

    }


    @Override
    public boolean isStarted() {
        return false;
    }


    @Override
    public boolean isShutdown() {
        return false;
    }


    @Override
    public void executeTask(Callable<T> task) {

    }


    @Override
    public void executeTask(Callable<T> task, long taskId) {

    }


    @Override
    public void executeTask(Callable<T> task, final long taskId, long ttl) {
        if (mIsStarted) {
            if (!mIsShutdown) {
                ListenableFuture<T> lf = mTaskExecutorService.submit(task);
                mTasksInFlight.put(taskId, new InFlightTtlHelper(taskId, getTime(), mTaskTtl, lf));
                Futures.addCallback(lf, new FutureCallback<T>() {
                    @Override
                    public void onSuccess(T result) {
                        notifySuccess(result, taskId);
                    }


                    @Override
                    public void onFailure(Throwable t) {
                        notifyFailure(taskId);
                    }
                });
            } else {
                throw new RejectedExecutionException("Already shutdown");
            }
        } else {
            throw new RejectedExecutionException("You forgot to call start() before this execute()");
        }
    }


    private void notifySuccess(T result, long taskId) {
        for(Listener<T> l : mListeners) {
            l.onTaskSuccess(result, taskId);
        }
    }


    private void notifyFailure(long taskId) {
        for(Listener l : mListeners) {
            l.onTaskFailure(taskId);
        }
    }


    @Override
    public void cancelTask(long taskId) {

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

    private void checkAndRemoveTtled() {
        for (InFlightTtlHelper<T> hlp : mTasksInFlight.values()) {
            if (hlp.mStartedAt + hlp.mTtl < getTime()) {
                mLogger.debug("Exchange {} TTLed", hlp.mTaskId);
                hlp.mFuture.cancel(true);
                notifyFailure(hlp.mTaskId);
            }
        }
    }


    private static class InFlightTtlHelper<T> {
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


    private long getTime() {
        return System.nanoTime() / 1000000;
    }
}
