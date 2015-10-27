package com.bolyartech.forge.rest;

import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by ogre on 2015-10-12
 */
public class KhRestFunctionalityImplTest {
    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(KhRestFunctionalityImpl.class
            .getSimpleName());


    @Test
    public void test_generateXId() {
        RestFunctionality rest = mock(RestFunctionality.class);

        KhRestFunctionalityImpl khRest = new KhRestFunctionalityImpl(rest);

        Long id = khRest.generateXId();
        assertTrue(id == 1);
    }


    @Test
    public void test_start() {
        RestFunctionality rest = mock(RestFunctionality.class);

        KhRestFunctionalityImpl khRest = new KhRestFunctionalityImpl(rest);

        assertFalse(khRest.isStarted());
        khRest.start();
        assertTrue(khRest.isStarted());

        khRest.shutdown();
    }


    @Test
    public void test_shutdown() {
        RestFunctionality rest = mock(RestFunctionality.class);

        KhRestFunctionalityImpl khRest = new KhRestFunctionalityImpl(rest);

        assertFalse(khRest.isStarted());
        khRest.start();
        khRest.shutdown();
        assertTrue(khRest.isShutdown());
    }


    @Test
    public void test_addListener() {
        RestFunctionality rest = mock(RestFunctionality.class);

        KhRestFunctionalityImpl khRest = new KhRestFunctionalityImpl(rest);

        KhRestFunctionality.Listener listener = mock(KhRestFunctionality.Listener.class);
        khRest.addListener(listener);

        List<KhRestFunctionality.Listener> listeners = khRest.getListeners();
        assertTrue(listeners.get(0) == listener);

        // not added more than once
        assertTrue(listeners.size() == 1);
    }


    @Test
    public void test_removeListener() {
        RestFunctionality rest = mock(RestFunctionality.class);

        KhRestFunctionalityImpl khRest = new KhRestFunctionalityImpl(rest);

        KhRestFunctionality.Listener listener = mock(KhRestFunctionality.Listener.class);
        khRest.addListener(listener);
        khRest.removeListener(listener);

        List<KhRestFunctionality.Listener> listeners = khRest.getListeners();
        assertTrue(listeners.size() == 0);
    }


    @Test(expected = RejectedExecutionException.class)
    public void test_executeKhRestExchange1() {
        RestFunctionality rest = mock(RestFunctionality.class);
        ExecutorService exec = mock(ExecutorService.class);

        KhRestFunctionalityImpl khRest = new KhRestFunctionalityImpl(rest, exec);

        RestExchange<KhRestResult> x = mock(RestExchange.class);
        khRest.executeKhRestExchange(x, 1l);
    }


    /**
     * Tests if outcome is correct when exchange is executed successfully
     *
     * @throws RestExchangeFailedException
     */
    @Test
    public void test_executeKhRestExchange2() throws RestExchangeFailedException {
        mLogger.trace("trace");
        mLogger.info("info");

        RestExchange<KhRestResult> x = mock(RestExchange.class);
        final KhRestResult rez = mock(KhRestResult.class);
        RestFunctionality rest = mock(RestFunctionality.class);
        when(rest.execute(x)).thenReturn(rez);
        ExecutorService exec = new DirectExecutorService();

        final boolean[] isListenerCalled = new boolean[1];
        isListenerCalled[0] = false;

        KhRestFunctionality.Listener listener = new KhRestFunctionality.Listener() {
            @Override
            public void onKhRestExchangeCompleted(RestExchangeOutcome<KhRestResult> out, long idL) {
                assertTrue(!out.isError());
                assertTrue(out.getResult() == rez);
                isListenerCalled[0] = true;
            }
        };
        KhRestFunctionalityImpl khRest = new KhRestFunctionalityImpl(rest, exec);
        khRest.addListener(listener);
        khRest.start();
        khRest.executeKhRestExchange(x, 1l);

        khRest.shutdown();

        assertTrue(isListenerCalled[0]);
    }


    /**
     * Tests if outcome is error when exchange is executed with error
     *
     * @throws RestExchangeFailedException
     */
    @Test
    public void test_executeKhRestExchange3() throws RestExchangeFailedException {
        RestExchange<KhRestResult> x = mock(RestExchange.class);
        RestFunctionality rest = mock(RestFunctionality.class);
        when(rest.execute(x)).thenThrow(new RestExchangeFailedException());
        ExecutorService exec = new DirectExecutorService();

        final boolean[] isListenerCalled = new boolean[1];
        isListenerCalled[0] = false;

        KhRestFunctionality.Listener listener = new KhRestFunctionality.Listener() {
            @Override
            public void onKhRestExchangeCompleted(RestExchangeOutcome<KhRestResult> out, long idL) {
                assertTrue(out.isError());
                isListenerCalled[0] = true;
            }
        };
        KhRestFunctionalityImpl khRest = new KhRestFunctionalityImpl(rest, exec);
        khRest.addListener(listener);
        khRest.start();
        khRest.executeKhRestExchange(x, 1l);
        khRest.shutdown();

        assertTrue(isListenerCalled[0]);
    }


    /**
     * Test if exchange is TTLed when it should be
     *
     * @throws RestExchangeFailedException
     * @throws InterruptedException
     */
    @Test
    public void test_ttled() throws RestExchangeFailedException, InterruptedException {
        RestExchange<KhRestResult> x = mock(RestExchange.class);
        final KhRestResult rez = mock(KhRestResult.class);
        RestFunctionality rest = mock(RestFunctionality.class);
//        when(rest.execute(x)).thenReturn(rez);
        ExecutorService exec = new DirectExecutorService();


        final RestExchangeOutcome[] isListenerCalled = new RestExchangeOutcome[1];

        KhRestFunctionality.Listener listener = new KhRestFunctionality.Listener() {
            @Override
            public void onKhRestExchangeCompleted(RestExchangeOutcome<KhRestResult> out, long idL) {
                isListenerCalled[0] = out;
                assertTrue(out.isError());
            }
        };

        KhRestFunctionalityImpl.Builder b = KhRestFunctionalityImpl.Builder.create(rest);
        KhRestFunctionalityImpl khRest = b.exchangeTtl(1).ttlCheckInterval(50).executorService(new DirectExecutorService()).build();
        khRest.addListener(listener);
        khRest.start();
        khRest.executeKhRestExchange(x, 1l);

        Thread.sleep(200);

        // listener called
        assertTrue(isListenerCalled[0] != null);

        khRest.shutdown();
    }


    /**
     * Checks that exchange is NOT TTLed prematurely
     *
     * @throws RestExchangeFailedException
     * @throws InterruptedException
     */
    @Test
    public void test_not_ttled() throws RestExchangeFailedException, InterruptedException {
        RestExchange<KhRestResult> x = mock(RestExchange.class);
        final KhRestResult rez = mock(KhRestResult.class);
        RestFunctionality rest = mock(RestFunctionality.class);
        when(rest.execute(x)).thenReturn(rez);
        ExecutorService exec = new DirectExecutorService();


        final RestExchangeOutcome[] isListenerCalled = new RestExchangeOutcome[1];

        KhRestFunctionality.Listener listener = new KhRestFunctionality.Listener() {
            @Override
            public void onKhRestExchangeCompleted(RestExchangeOutcome<KhRestResult> out, long idL) {
                isListenerCalled[0] = out;
                assertTrue(!out.isError());
            }
        };

        KhRestFunctionalityImpl.Builder b = KhRestFunctionalityImpl.Builder.create(rest);
        KhRestFunctionalityImpl khRest = b.exchangeTtl(3000).ttlCheckInterval(50).executorService(new DirectExecutorService()).build();
        khRest.addListener(listener);
        khRest.start();
        khRest.executeKhRestExchange(x, 1l);

        Thread.sleep(200);

        // listener called
        assertTrue(isListenerCalled[0] != null);

        khRest.shutdown();
    }


//    @Test
//    public void test_consumeExchange() throws RestExchangeFailedException, InterruptedException {
//        RestExchange<KhRestResult> x = mock(RestExchange.class);
//        final KhRestResult rez = mock(KhRestResult.class);
//        RestFunctionality rest = mock(RestFunctionality.class);
//        when(rest.execute(x)).thenReturn(rez);
//        ExecutorService exec = new DirectExecutorService();
//
//        KhRestFunctionalityImpl.Builder b = KhRestFunctionalityImpl.Builder.create(rest);
//        KhRestFunctionalityImpl khRest = b.exchangeTtl(1).executorService(new DirectExecutorService()).build();
//
//        khRest.start();
//        khRest.executeKhRestExchange(x, 1l);
//
//        // first assure that exchange is not ttled prematurely
//        assertTrue(khRest.getCompletedExchanges().size() == 1);
//
//        khRest.consumeExchange(1l);
//
//        // assure that is removed
//        assertTrue(khRest.getCompletedExchanges().size() == 0);
//
//
//        khRest.shutdown();
//    }


    private class DirectExecutorService implements ExecutorService {

        @Override
        public void shutdown() {

        }


        @Override
        public List<Runnable> shutdownNow() {
            return null;
        }


        @Override
        public boolean isShutdown() {
            return false;
        }


        @Override
        public boolean isTerminated() {
            return false;
        }


        @Override
        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return false;
        }


        @Override
        public <T> Future<T> submit(Callable<T> task) {
            return null;
        }


        @Override
        public <T> Future<T> submit(Runnable task, T result) {
            return null;
        }


        @Override
        public Future<?> submit(Runnable task) {
            return null;
        }


        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
            return null;
        }


        @Override
        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit
        ) throws InterruptedException {
            return null;
        }


        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
            return null;
        }


        @Override
        public <T> T invokeAny(Collection<? extends Callable<T>> tasks,
                               long timeout,
                               TimeUnit unit
        ) throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }


        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }
}
