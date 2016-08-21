package com.bolyartech.forge.base.task;

import com.bolyartech.forge.base.exchange.forge.ForgeExchangeResult;
import com.bolyartech.forge.base.misc.TimeProvider;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public class ForgeTaskExecutor extends TaskExecutorImpl<ForgeExchangeResult> {
    @Inject
    public ForgeTaskExecutor() {
    }


    public ForgeTaskExecutor(ExecutorService taskExecutorService, ScheduledExecutorService scheduler) {
        super(taskExecutorService, scheduler);
    }


    public ForgeTaskExecutor(ExecutorService taskExecutorService, int ttlCheckInterval, int taskTtl, ScheduledExecutorService scheduler, TimeProvider timeProvider) {
        super(taskExecutorService, ttlCheckInterval, taskTtl, scheduler, timeProvider);
    }
}
