package com.bolyartech.forge.base.task;

import com.bolyartech.forge.base.exchange.forge.ForgeExchangeResult;
import com.bolyartech.forge.base.misc.TimeProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import javax.inject.Inject;


/**
 * Executor for tasks that produce result of type ForgeExchangeResult
 */
public class ForgeTaskExecutor extends TaskExecutorImpl<ForgeExchangeResult> {
    /**
     * Creates new ForgeTaskExecutor
     */
    @SuppressWarnings("unused")
    @Inject
    public ForgeTaskExecutor() {
    }


    /**
     * Creates new ForgeTaskExecutor
     * @param taskExecutorService Task executor service to be used
     * @param scheduler Scheduler service to be used
     */
    @SuppressWarnings("unused")
    public ForgeTaskExecutor(ExecutorService taskExecutorService, ScheduledExecutorService scheduler) {
        super(taskExecutorService, scheduler);
    }


    /**
     * Creates new ForgeTaskExecutor
     * @param taskExecutorService Task executor service to be used
     * @param ttlCheckInterval Interval at which check for TTLed task will run
     * @param taskTtl TTL for task
     * @param scheduler Scheduler service to be used
     * @param timeProvider Time provider
     */
    @SuppressWarnings("unused")
    public ForgeTaskExecutor(ExecutorService taskExecutorService,
                             int ttlCheckInterval,
                             int taskTtl,
                             ScheduledExecutorService scheduler,
                             TimeProvider timeProvider) {
        super(taskExecutorService, ttlCheckInterval, taskTtl, scheduler, timeProvider);
    }
}
