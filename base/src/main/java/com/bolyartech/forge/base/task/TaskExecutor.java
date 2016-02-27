package com.bolyartech.forge.base.task;

import java.util.concurrent.Callable;


/**
 * Created by ogre on 2016-01-12 10:37
 */
public interface TaskExecutor<T> {
    void start();
    void shutdown();
    boolean isStarted();
    boolean isShutdown();

    void executeTask(Callable<T> task);
    void executeTask(Callable<T> task, long taskId);
    void executeTask(Callable<T> task, long taskId, long ttl);

    void cancelTask(long taskId, boolean mayInterruptIfRunning);

    void addListener(Listener<T> listener);
    void removeListener(Listener<T> listener);

    Long generateTaskId();

    interface Listener<T> {
        /**
         * Called then exchange is completed (either successful or not)
         *
         * @param taskId ID of the exchange
         */
        void onTaskSuccess(long taskId, T result);
        void onTaskFailure(long taskId);
    }
}
