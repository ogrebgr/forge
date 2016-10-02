package com.bolyartech.forge.base.task;

import com.bolyartech.forge.base.exchange.Exchange;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Implementation of ExchangeManager
 * @param <T> Type of the result of the exchanges
 */
@SuppressWarnings("WeakerAccess")
public class ExchangeManagerImpl<T> implements ExchangeManager<T>, TaskExecutor.Listener<T> {
    private TaskExecutor<T> mTaskExecutor;

    private final List<Listener<T>> mListeners = new CopyOnWriteArrayList<>();

    private volatile boolean mStarted = false;


    /**
     * Creates new ExchangeManagerImpl
     */
    public ExchangeManagerImpl() {
    }


    @Override
    public synchronized void start(TaskExecutor<T> taskExecutor) {
        mStarted = true;
        mTaskExecutor = taskExecutor;
        mTaskExecutor.addListener(this);

        mTaskExecutor.start();
    }


    @Override
    public synchronized void shutdown() {
        if (mStarted) {
            mStarted = false;
            mTaskExecutor.shutdown();
            mTaskExecutor.removeListener(this);
        }
    }


    @Override
    public void addListener(Listener<T> listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }


    @Override
    public void removeListener(Listener listener) {
        mListeners.remove(listener);
    }


    @Override
    public void executeExchange(Exchange<T> x) {
        executeExchange(x, generateTaskId());
    }


    @Override
    public void executeExchange(Exchange<T> x, Long taskId) {
        Callable<T> c = createCallable(x);
        mTaskExecutor.executeTask(c, taskId);
    }


    @Override
    public void executeExchange(Exchange<T> x, Long taskId, long ttl) {
        mTaskExecutor.executeTask(createCallable(x), taskId, ttl);
    }


    @Override
    public void cancelExchange(Long xId) {
        mTaskExecutor.cancelTask(xId, true);
    }


    @Override
    public Long generateTaskId() {
        return mTaskExecutor.generateTaskId();
    }


    @Override
    public void onTaskSuccess(long taskId, T result) {
        for(Listener<T> l : mListeners) {
            l.onExchangeOutcome(taskId, true, result);
        }
    }


    @Override
    public void onTaskFailure(long taskId) {
        for(Listener<T> l : mListeners) {
            l.onExchangeOutcome(taskId, false, null);
        }
    }


    private Callable<T> createCallable(final Exchange<T> x) {
        return new Callable<T>() {
            @Override
            public T call() throws Exception {
                return x.execute();
            }
        };
    }


    @Override
    public boolean isStarted() {
        return mStarted;
    }
}
