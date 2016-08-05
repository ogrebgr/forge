package com.bolyartech.forge.base.task;

import com.bolyartech.forge.base.misc.TimeProvider;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import java.util.concurrent.*;


@SuppressWarnings("unchecked")
public class TaskExecutorImplTest {
    private volatile boolean mListenerCalled;
    private volatile long mTaskId;


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


    @Test
    public void testExecuteSuccess() throws Exception {
        ExecutorService es = MoreExecutors.newDirectExecutorService();
        ScheduledExecutorService sch = mock(ScheduledExecutorService.class);

        TaskExecutorImpl te = new TaskExecutorImpl(es, sch);
        te.addListener(new TaskExecutor.Listener() {
            @Override
            public void onTaskSuccess(long taskId, Object result) {
                mListenerCalled = true;
            }


            @Override
            public void onTaskFailure(long taskId) {

            }
        });
        Callable<String> c = mock(Callable.class);
        when(c.call()).thenReturn("presni");
        te.start();

        mListenerCalled = false;
        te.executeTask(c, 1, 1);

        assertTrue("onTaskSuccess() not called", mListenerCalled);
    }


    @Test
    public void testExecuteFail() throws Exception {
        ExecutorService es = MoreExecutors.newDirectExecutorService();
        ScheduledExecutorService sch = mock(ScheduledExecutorService.class);

        TaskExecutorImpl te = new TaskExecutorImpl(es, sch);
        te.addListener(new TaskExecutor.Listener() {
            @Override
            public void onTaskSuccess(long taskId, Object result) {

            }


            @Override
            public void onTaskFailure(long taskId) {
                mListenerCalled = true;
            }
        });
        Callable<String> c = mock(Callable.class);
        when(c.call()).thenThrow(new RuntimeException());
        te.start();

        mListenerCalled = false;
        te.executeTask(c, 1, 1);

        assertTrue("onTaskSuccess() not called", mListenerCalled);
    }


    @Test
    public void testIsTtled() throws Exception {
        ListenableFuture<String> f = mock(ListenableFuture.class);
        TaskExecutorImpl.InFlightTtlHelper<String> hlp = new TaskExecutorImpl.InFlightTtlHelper(1, 1, 1, f);
        TimeProvider tp = mock(TimeProvider.class);
        when(tp.getTime()).thenReturn(100L);
        assertTrue("Not TTLed", TaskExecutorImpl.isTtled(hlp, tp));
    }


    @Test
    public void testIsNotTtled() throws Exception {
        ListenableFuture<String> f = mock(ListenableFuture.class);
        TaskExecutorImpl.InFlightTtlHelper<String> hlp = new TaskExecutorImpl.InFlightTtlHelper(1, 1, 1000, f);
        TimeProvider tp = mock(TimeProvider.class);
        when(tp.getTime()).thenReturn(100L);
        assertFalse("TTLed when it should not", TaskExecutorImpl.isTtled(hlp, tp));
    }


    @Test
    public void testExecuteExpire() {
        ExecutorService es = mock(ExecutorService.class);
        ScheduledExecutorService sch = mock(ScheduledExecutorService.class);

        TaskExecutorImpl te = new TaskExecutorImpl(es, sch);
        te.addListener(new TaskExecutor.Listener() {
            @Override
            public void onTaskSuccess(long taskId, Object result) {

            }


            @Override
            public void onTaskFailure(long taskId) {
                mTaskId = taskId;
            }
        });
        te.start();


        Callable<String> task = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "123";
            }
        };

        mTaskId = 0;
        te.executeTask(task, 123, 0);
        te.checkAndRemoveTtled();

        assertTrue("Not expired when it should", mTaskId == 123);
    }


    @Test
    public void testExecuteNotExpire() {
        ExecutorService es = Executors.newSingleThreadExecutor();
        ScheduledExecutorService sch = mock(ScheduledExecutorService.class);

        TaskExecutorImpl te = new TaskExecutorImpl(es, sch);
        te.addListener(new TaskExecutor.Listener() {
            @Override
            public void onTaskSuccess(long taskId, Object result) {

            }


            @Override
            public void onTaskFailure(long taskId) {
                mTaskId = taskId;
            }
        });
        te.start();


        Callable<String> task = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(100);
                return "123";
            }
        };

        mTaskId = 0;
        te.executeTask(task, 123, 500);
        te.checkAndRemoveTtled();

        assertTrue("Expired when it should not", mTaskId == 0);
    }

    @Test
    public void testCancelTask() {
        ExecutorService es = Executors.newSingleThreadExecutor();
        ScheduledExecutorService sch = mock(ScheduledExecutorService.class);

        TaskExecutorImpl te = new TaskExecutorImpl(es, sch);
        te.addListener(new TaskExecutor.Listener() {
            @Override
            public void onTaskSuccess(long taskId, Object result) {
                mTaskId = taskId;
            }


            @Override
            public void onTaskFailure(long taskId) {
                mTaskId = taskId;
            }
        });
        te.start();


        Callable<String> task = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(100);
                return "123";
            }
        };

        mTaskId = 0;
        te.executeTask(task, 123, 500);
        te.cancelTask(123, true);

        assertTrue("Not cancelled", mTaskId == 0);
    }
}
