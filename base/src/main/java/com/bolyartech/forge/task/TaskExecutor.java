package com.bolyartech.forge.task;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javafx.concurrent.Task;


/**
 * Created by ogre on 2016-01-12 10:37
 */
public interface TaskExecutor {
    void start();
    void shutdown();
    boolean isStarted();
    boolean isShutdown();

    ListenableFuture<?> executeTask(Callable<?> task);
    ListenableFuture<?> executeTask(Callable<?> task, long taskId);
    ListenableFuture<?> executeTask(Callable<?> task, long taskId, long ttl);

    void cancelTask(long taskId);

    void addListener(Listener<?> listener);
    void removeListener(Listener<?> listener);

    Long generateTaskId();

    interface Listener<T> {
        /**
         * Called then exchange is completed (either successful or not)
         *
         * @param taskId ID of the exchange
         */
        void onTaskSuccess(long taskId);
        void onTaskFailure(long taskId);
    }
}
