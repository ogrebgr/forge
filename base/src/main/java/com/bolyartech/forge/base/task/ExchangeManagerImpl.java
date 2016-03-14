package com.bolyartech.forge.base.task;

import com.bolyartech.forge.base.exchange.Exchange;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;


public class ExchangeManagerImpl<T> implements ExchangeManager<T>, TaskExecutor.Listener<T> {
    private final TaskExecutor<T> mTaskExecutor;

    private final List<Listener<T>> mListeners = new CopyOnWriteArrayList<>();


    public ExchangeManagerImpl(TaskExecutor<T> taskExecutor) {
        mTaskExecutor = taskExecutor;
        mTaskExecutor.addListener(this);
    }


    @Override
    public void start() {
        mTaskExecutor.start();
    }


    @Override
    public void shutdown() {
        mTaskExecutor.shutdown();
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
}
