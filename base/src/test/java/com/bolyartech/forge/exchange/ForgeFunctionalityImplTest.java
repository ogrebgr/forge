package com.bolyartech.forge.exchange;

import com.bolyartech.forge.http.functionality.HttpFunctionality;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
public class ForgeFunctionalityImplTest {
    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(ExchangeFunctionalityImpl.class
            .getSimpleName());


    @Test
    public void test_generateXId() {
        HttpFunctionality rest = mock(HttpFunctionality.class);

        ExchangeFunctionalityImpl khRest = new ExchangeFunctionalityImpl(rest);

        Long id = khRest.generateXId();
        assertTrue(id == 1);
    }


    @Test
    public void test_start() {
        HttpFunctionality rest = mock(HttpFunctionality.class);

        ExchangeFunctionalityImpl khRest = new ExchangeFunctionalityImpl(rest);

        assertFalse(khRest.isStarted());
        khRest.start();
        assertTrue(khRest.isStarted());

        khRest.shutdown();
    }


    @Test
    public void test_shutdown() {
        HttpFunctionality rest = mock(HttpFunctionality.class);

        ExchangeFunctionalityImpl khRest = new ExchangeFunctionalityImpl(rest);

        assertFalse(khRest.isStarted());
        khRest.start();
        khRest.shutdown();
        assertTrue(khRest.isShutdown());
    }


    @Test
    public void test_addListener() {
        HttpFunctionality rest = mock(HttpFunctionality.class);

        ExchangeFunctionalityImpl khRest = new ExchangeFunctionalityImpl(rest);

        ExchangeFunctionalityImpl.Listener listener = mock(ExchangeFunctionalityImpl.Listener.class);
        khRest.addListener(listener);

        List<ExchangeFunctionalityImpl.Listener> listeners = khRest.getListeners();
        assertTrue(listeners.get(0) == listener);

        // not added more than once
        assertTrue(listeners.size() == 1);
    }


    @Test
    public void test_removeListener() {
        HttpFunctionality rest = mock(HttpFunctionality.class);

        ExchangeFunctionalityImpl khRest = new ExchangeFunctionalityImpl(rest);

        ExchangeFunctionalityImpl.Listener listener = mock(ExchangeFunctionalityImpl.Listener.class);
        khRest.addListener(listener);
        khRest.removeListener(listener);

        List<ExchangeFunctionalityImpl.Listener> listeners = khRest.getListeners();
        assertTrue(listeners.size() == 0);
    }


    @Test(expected = RejectedExecutionException.class)
    public void test_executeKhRestExchange1() {
        HttpFunctionality http = mock(HttpFunctionality.class);
        ExecutorService exec = mock(ExecutorService.class);

        ExchangeFunctionalityImpl khRest = new ExchangeFunctionalityImpl(http, exec);

        Exchange<ForgeExchangeResult> x = mock(Exchange.class);
        khRest.executeExchange(x, 1l);
    }


    /**
     * Tests if outcome is correct when exchange is executed successfully
     *
     * @throws RestExchangeFailedException
     */
    @Test
    public void test_executeKhRestExchange2() throws RestExchangeFailedException, IOException, ResultProducer.ResultProducerException {
        mLogger.trace("trace");
        mLogger.info("info");

        Exchange<ForgeExchangeResult> x = mock(Exchange.class);
        final ForgeExchangeResult rez = mock(ForgeExchangeResult.class);
        HttpFunctionality http = mock(HttpFunctionality.class);
        when(x.execute()).thenReturn(rez);
        ExecutorService exec = new DirectExecutorService();

        final boolean[] isListenerCalled = new boolean[1];
        isListenerCalled[0] = false;

        ExchangeFunctionalityImpl.Listener listener = new ExchangeFunctionalityImpl.Listener() {
            @Override
            public void onExchangeCompleted(ExchangeOutcome outcome, long exchangeId) {
                assertTrue(!outcome.isError());
                assertTrue(outcome.getResult() == rez);
                isListenerCalled[0] = true;
            }
        };
        ExchangeFunctionalityImpl khRest = new ExchangeFunctionalityImpl(http, exec);
        khRest.addListener(listener);
        khRest.start();
        khRest.executeExchange(x, 1l);

        khRest.shutdown();

        assertTrue(isListenerCalled[0]);
    }


    /**
     * Tests if outcome is error when exchange is executed with error
     *
     * @throws RestExchangeFailedException
     */
    @Test
    public void test_executeKhRestExchange3() throws RestExchangeFailedException, IOException, ResultProducer.ResultProducerException {
        Exchange<ForgeExchangeResult> x = mock(Exchange.class);
        HttpFunctionality http = mock(HttpFunctionality.class);
        when(x.execute()).thenThrow(new IOException());
        ExecutorService exec = new DirectExecutorService();

        final boolean[] isListenerCalled = new boolean[1];
        isListenerCalled[0] = false;

        ExchangeFunctionalityImpl.Listener listener = new ExchangeFunctionalityImpl.Listener() {
            @Override
            public void onExchangeCompleted(ExchangeOutcome outcome, long exchangeId) {
                assertTrue(outcome.isError());
                isListenerCalled[0] = true;
            }
        };
        ExchangeFunctionalityImpl khRest = new ExchangeFunctionalityImpl(http, exec);
        khRest.addListener(listener);
        khRest.start();
        khRest.executeExchange(x, 1l);
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
        Exchange<ForgeExchangeResult> x = mock(Exchange.class);
        final ForgeExchangeResult rez = mock(ForgeExchangeResult.class);
        HttpFunctionality rest = mock(HttpFunctionality.class);
//        when(exchange.execute(x)).thenReturn(rez);
        ExecutorService exec = new DirectExecutorService();


        final ExchangeOutcome[] isListenerCalled = new ExchangeOutcome[1];

        ExchangeFunctionality.Listener<ForgeExchangeResult> listener = new ExchangeFunctionality.Listener<ForgeExchangeResult>() {
            @Override
            public void onExchangeCompleted(ExchangeOutcome<ForgeExchangeResult> outcome, long exchangeId) {
                isListenerCalled[0] = outcome;
                assertTrue(outcome.isError());
            }
        };

        ExchangeFunctionalityImpl.Builder b = ExchangeFunctionalityImpl.Builder.create(rest);
        ExchangeFunctionalityImpl khRest = b.exchangeTtl(1).ttlCheckInterval(50).executorService(new DirectExecutorService()).build();
        khRest.addListener(listener);
        khRest.start();
        khRest.executeExchange(x, 1l);

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
    public void test_not_ttled() throws RestExchangeFailedException, InterruptedException, IOException, ResultProducer.ResultProducerException {
        Exchange<ForgeExchangeResult> x = mock(Exchange.class);
        final ForgeExchangeResult rez = mock(ForgeExchangeResult.class);
        HttpFunctionality http = mock(HttpFunctionality.class);
        when(x.execute()).thenReturn(rez);
        ExecutorService exec = new DirectExecutorService();


        final ExchangeOutcome[] isListenerCalled = new ExchangeOutcome[1];

        ExchangeFunctionalityImpl.Listener listener = new ExchangeFunctionalityImpl.Listener() {
            @Override
            public void onExchangeCompleted(ExchangeOutcome outcome, long exchangeId) {
                isListenerCalled[0] = outcome;
                assertTrue(!outcome.isError());
            }
        };

        ExchangeFunctionalityImpl.Builder b = ExchangeFunctionalityImpl.Builder.create(http);
        ExchangeFunctionalityImpl khRest = b.exchangeTtl(3000).ttlCheckInterval(50).executorService(new DirectExecutorService()).build();
        khRest.addListener(listener);
        khRest.start();
        khRest.executeExchange(x, 1l);

        Thread.sleep(200);

        // listener called
        assertTrue(isListenerCalled[0] != null);

        khRest.shutdown();
    }


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
