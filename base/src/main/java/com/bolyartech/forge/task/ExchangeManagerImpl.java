package com.bolyartech.forge.task;

import com.bolyartech.forge.exchange.Exchange;
import com.bolyartech.forge.http.functionality.HttpFunctionality;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Created by ogre on 2016-01-12 13:21
 */
public class ExchangeManagerImpl<T> implements ExchangeManager<T>, TaskExecutor.Listener<T> {
    private final TaskExecutor<T> mTaskExecutor;
    private final HttpFunctionality mHttpFunc;

    private final List<Listener<T>> mListeners = new CopyOnWriteArrayList<>();


    public ExchangeManagerImpl(TaskExecutor<T> taskExecutor, HttpFunctionality httpFunc) {
        mTaskExecutor = taskExecutor;
        mHttpFunc = httpFunc;
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
        mTaskExecutor.executeTask(createCallable(x));
    }


    @Override
    public void executeExchange(Exchange<T> x, Long xId) {
        mTaskExecutor.executeTask(createCallable(x), xId);
    }


    @Override
    public void executeExchange(Exchange<T> x, Long xId, long ttl) {
        mTaskExecutor.executeTask(createCallable(x), xId, ttl);
    }


    @Override
    public Long generateXId() {
        return mTaskExecutor.generateTaskId();
    }


    @Override
    public void onTaskSuccess(T result, long taskId) {
        for(Listener<T> l : mListeners) {
           l.onExchangeOutcome(taskId, true, result);
        }
    }


    @Override
    public void onTaskFailure(long taskId) {
        for(Listener<T> l : mListeners) {
            l.onExchangeOutcome(taskId, true, null);
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
