package com.bolyartech.forge.task;

import com.bolyartech.forge.exchange.Exchange;
import com.bolyartech.forge.http.functionality.HttpFunctionality;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;


/**
 * Created by ogre on 2016-01-12 13:21
 */
public class ExchangeManagerImpl<T> implements ExchangeManager<T>, TaskExecutor.Listener<T> {
    private final TaskExecutor mTaskExecutor;
    private final HttpFunctionality mHttpFunc;

    private final List<Listener<T>> mListeners = new CopyOnWriteArrayList<>();
    private final Map<Long, ListenableFuture<T>> mTasks = new ConcurrentHashMap<>();


    public ExchangeManagerImpl(TaskExecutor taskExecutor, HttpFunctionality httpFunc) {
        mTaskExecutor = taskExecutor;
        mTaskExecutor.addListener(this);
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
        executeExchange(x, generateTaskId());
    }


    @Override
    public void executeExchange(Exchange<T> x, Long taskId) {
        // Suppressed because we are calling with  Exchange<T>
        @SuppressWarnings("unchecked")
        ListenableFuture<T> f = (ListenableFuture<T>) mTaskExecutor.executeTask(createCallable(x), taskId);

        mTasks.put(taskId, f);
    }


    @Override
    public synchronized void executeExchange(Exchange<T> x, Long taskId, long ttl) {
        // Suppressed because we are calling with  Exchange<T>
        @SuppressWarnings("unchecked")
        ListenableFuture<T> f = (ListenableFuture<T>) mTaskExecutor.executeTask(createCallable(x), taskId, ttl);

        mTasks.put(taskId, f);
    }


    @Override
    public Long generateTaskId() {
        return mTaskExecutor.generateTaskId();
    }


    @Override
    public synchronized void onTaskSuccess(long taskId) {
        ListenableFuture<T> f = mTasks.get(taskId);

        for(Listener<T> l : mListeners) {
            try {
                l.onExchangeOutcome(taskId, true, f.get());
            } catch (InterruptedException | ExecutionException e) {
                // cannot happen because id success there is no ExecutionException possible
                // and task is completed so no InterruptedException is possible
                throw new RuntimeException(e);
            }
        }
        mTasks.remove(taskId);
    }


    @Override
    public synchronized void onTaskFailure(long taskId) {
        for(Listener<T> l : mListeners) {
            l.onExchangeOutcome(taskId, false, null);
        }
        mTasks.remove(taskId);
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
