package com.bolyartech.forge.base.task;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;


@SuppressWarnings("unchecked")
public class TaskExecutorImplTest {

    @Test
    public void testStartStop() {
        ExecutorService es = mock(ExecutorService.class);
        ScheduledExecutorService sch = mock(ScheduledExecutorService.class);

        TaskExecutorImpl te = new TaskExecutorImpl(es, sch);
        te.start();
        assertTrue("Not started", te.isStarted());
        te.shutdown();
        assertTrue("Not shutdown", te.isShutdown());
    }


    @Test
    public void testGenerateId() {
        ExecutorService es = mock(ExecutorService.class);
        ScheduledExecutorService sch = mock(ScheduledExecutorService.class);

        TaskExecutorImpl te = new TaskExecutorImpl(es, sch);
        te.start();

        long id = te.generateTaskId();
        assertTrue("unexpeted id", id == 1);
    }


    @Test(expected = RejectedExecutionException.class)
    public void testExecuteNotStarted() {
        ExecutorService es = mock(ExecutorService.class);
        ScheduledExecutorService sch = mock(ScheduledExecutorService.class);

        TaskExecutorImpl te = new TaskExecutorImpl(es, sch);
        Callable<String> c = mock(Callable.class);


        te.executeTask(c, 1, 1);
    }


    @Test(expected = RejectedExecutionException.class)
    public void testExecuteShutdowned() {
        ExecutorService es = mock(ExecutorService.class);
        ScheduledExecutorService sch = mock(ScheduledExecutorService.class);

        TaskExecutorImpl te = new TaskExecutorImpl(es, sch);
        Callable<String> c = mock(Callable.class);
        te.start();
        te.shutdown();

        te.executeTask(c, 1, 1);
    }



}
