package com.bolyartech.forge.base.exchange;

import com.bolyartech.forge.base.task.TaskExecutor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Implementation of ExchangeManager
 *
 * @param <T> Type of the result of the exchanges
 */
@SuppressWarnings("WeakerAccess")
public class ExchangeManagerImpl<T> implements ExchangeManager<T>, TaskExecutor.Listener<T> {
    private final List<Listener<T>> mListeners = new CopyOnWriteArrayList<>();
    private TaskExecutor<T> mTaskExecutor;
    private volatile boolean mStarted = false;

    private Map<Long, Exchange<?>> mInFlight = new ConcurrentHashMap<>();
    private Map<Long, ExchangeOutcomeHandler<T>> mExchangeOutcomeHandlers = new ConcurrentHashMap<>();


    /**
     * Creates new ExchangeManagerImpl
     */
    public ExchangeManagerImpl() {
    }


    @Override
    public synchronized void start(TaskExecutor<T> taskExecutor) {
        if (!mStarted) {
            mStarted = true;
            mTaskExecutor = taskExecutor;
            mTaskExecutor.addListener(this);

            mTaskExecutor.start();
        } else {
            throw new IllegalStateException("Already started");
        }
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
    public long executeExchange(Exchange<T> x) {
        long ret = generateTaskId();
        executeExchange(x, ret);
        return ret;
    }


    @Override
    public long executeExchange(Exchange<T> x, ExchangeOutcomeHandler<T> handler) {
        long ret = generateTaskId();
        mExchangeOutcomeHandlers.put(ret, handler);
        executeExchange(x, ret);
        return ret;
    }


    @Override
    public long executeExchange(Exchange<T> x, ExchangeOutcomeHandler<T> handler, long ttl) {
        long ret = generateTaskId();
        mExchangeOutcomeHandlers.put(ret, handler);
        executeExchange(x, ret, ttl);
        return ret;
    }


    @Override
    public void cancelExchange(Long xId) {
        Exchange<?> x = mInFlight.get(xId);
        if (x != null) {
            x.cancel();
            mInFlight.remove(xId);
        }
        mTaskExecutor.cancelTask(xId, true);
        mExchangeOutcomeHandlers.remove(xId);
    }


    @Override
    public void onTaskSuccess(long taskId, T result) {
        mInFlight.remove(taskId);

        ExchangeOutcomeHandler<T> handler = mExchangeOutcomeHandlers.remove(taskId);
        if (handler != null) {
            handler.handle(true, result);
        }

        for (Listener<T> l : mListeners) {
            l.onExchangeOutcome(taskId, true, result);
        }
    }


    @Override
    public void onTaskFailure(long taskId) {
        mInFlight.remove(taskId);

        ExchangeOutcomeHandler<T> handler = mExchangeOutcomeHandlers.remove(taskId);
        if (handler != null) {
            handler.handle(false, null);
        }

        for (Listener<T> l : mListeners) {
            l.onExchangeOutcome(taskId, false, null);
        }
    }


    @Override
    public boolean isStarted() {
        return mStarted;
    }


    private void executeExchange(Exchange<T> x, Long taskId) {
        mInFlight.put(taskId, x);
        Callable<T> c = createCallable(x);
        mTaskExecutor.executeTask(c, taskId);
    }


    private void executeExchange(Exchange<T> x, Long taskId, long ttl) {
        mTaskExecutor.executeTask(createCallable(x), taskId, ttl);
    }


    private Long generateTaskId() {
        return mTaskExecutor.generateTaskId();
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
