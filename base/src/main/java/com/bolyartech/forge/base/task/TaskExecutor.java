package com.bolyartech.forge.base.task;

import java.util.concurrent.Callable;


/**
 * Created by ogre on 2016-01-12 10:37
 */
public interface TaskExecutor<T> {
    /**
     * Starts the task executor
     */
    @SuppressWarnings("unused")
    void start();

    /**
     * Stops the task executor
     */
    void shutdown();

    /**
     * Checks if the task executor is started
     * @return True if executor is started
     */
    @SuppressWarnings("unused")
    boolean isStarted();

    /**
     * Checks if the task executor is shut down
     * @return True if executor is shut down
     */
    @SuppressWarnings("unused")
    boolean isShutdown();

    /**
     * Executes a task with internally generated task ID and with the default TTL
     * @param task Task to be executed
     */
    @SuppressWarnings("unused")
    void executeTask(Callable<T> task);

    /**
     * Executes a task with the default TTL
     * @param task Task to be executed
     * @param taskId ID of the task
     */
    void executeTask(Callable<T> task, long taskId);

    /**
     * Executes a task
     * @param task Task to be executed
     * @param taskId ID of the task
     * @param ttl TTL
     */
    @SuppressWarnings("unused")
    void executeTask(Callable<T> task, long taskId, long ttl);

    /**
     * Cancels the execution of the task if is still waiting for execution or currently executing
     * Neither {onTaskSuccess()} or {onTaskFailure()} will be called for this task
     * @param taskId ID of the task
     * @param mayInterruptIfRunning If true will try to interrupt the task if it is already running
     */
    @SuppressWarnings("unused")
    void cancelTask(long taskId, boolean mayInterruptIfRunning);

    /**
     * Adds listener
     * @param listener Listener to be added
     */
    @SuppressWarnings("unused")
    void addListener(Listener<T> listener);

    /**
     * Removes a listener
     * @param listener Listener to be removed
     */
    void removeListener(Listener<T> listener);


    /**
     * Generates unique id to be used with {executeTask()}.
     * @return Generated ID
     */
    Long generateTaskId();

    interface Listener<T> {
        /**
         * Called when exchange is completed successfully
         *
         * @param taskId ID of the exchange
         * @param result Object of type T which is the result of the task
         */
        void onTaskSuccess(long taskId, T result);

        /**
         * Called when exchange has failed
         * @param taskId ID of the exchange
         */
        void onTaskFailure(long taskId);
    }
}
