package com.bolyartech.forge.task;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javafx.concurrent.Task;


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

    void cancelTask(long taskId);

    void addListener(Listener<T> listener);
    void removeListener(Listener<T> listener);

    Long generateTaskId();

    interface Listener<T> {
        /**
         * Called then exchange is completed (either successful or not)
         *
         * @param taskId ID of the exchange
         */
        void onTaskSuccess(T result, long taskId);
        void onTaskFailure(long taskId);
    }
}
