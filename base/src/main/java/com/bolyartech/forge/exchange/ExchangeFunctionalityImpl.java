/*
 * Copyright (C) 2012-2015 Ognyan Bankov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bolyartech.forge.exchange;

import com.bolyartech.forge.http.functionality.HttpFunctionality;
import com.bolyartech.forge.misc.ForUnitTestsOnly;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;


@SuppressWarnings("WeakerAccess")
public class ExchangeFunctionalityImpl<T> implements ExchangeFunctionality<T> {
    private static final int TTL_CHECK_INTERVAL = 1000;
    private static final int DEFAULT_EXCHANGE_TTL = 5000;
    private static final int DEFAULT_EXECUTOR_SERVICE_THREADS = 2;

    private final HttpFunctionality mHttpFunc;

    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(ExchangeFunctionalityImpl.class
            .getSimpleName());

    private final AtomicLong mSequenceGenerator = new AtomicLong(0);
    private final List<Listener> mListeners = new CopyOnWriteArrayList<>();

    private final Map<Long, InFlightTtlHelper> mExchangesInFlight = new ConcurrentHashMap<>();

    private final ScheduledExecutorService mScheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @SuppressWarnings("NullableProblems")
        @Override
        public Thread newThread(Runnable r) {
            Thread ret = new Thread(r);
            ret.setName("ExchangeFunctionalityImpl Scheduler");
            return ret;
        }
    });
    private final ExecutorService mExchangeExecutorService;
    private final int mTtlCheckInterval;
    private final int mExchangeTtl;
    private ScheduledFuture<?> mTtlChecker;
    private volatile boolean mIsStarted = false;
    private volatile boolean mIsShutdown = false;


    /**
     * Creates new instance with default executor service (2 threads), default TTL check
     * interval (1000 millis), default exchange TTL (5000 millis (On slow network connections like GPRS that might not be enough)).
     *
     * @param httpFunc RestFunctionality implementation
     */
    public ExchangeFunctionalityImpl(HttpFunctionality httpFunc) {
        this(httpFunc,
                createDefaultExecutorService(),
                TTL_CHECK_INTERVAL,
                DEFAULT_EXCHANGE_TTL
        );
    }


    /**
     * Creates new intance with specified executor service and with default exchange TTL (5000
     * millis (On slow network connections like GPRS that might not be enough)).
     *
     * @param httpFunc                RestFunctionality implementation
     * @param exchangeExecutorService ExecutorService provided by you
     */
    public ExchangeFunctionalityImpl(HttpFunctionality httpFunc, ExecutorService exchangeExecutorService) {
        this(httpFunc,
                exchangeExecutorService,
                TTL_CHECK_INTERVAL,
                DEFAULT_EXCHANGE_TTL
        );
    }


    /**
     * @param httpFunc                RestFunctionality implementation
     * @param exchangeExecutorService ExecutorService provided by you
     * @param ttlCheckInterval        Interval between checks for TTLed exchanges
     * @param exchangeTtl             default TTL for the exchanges
     */
    public ExchangeFunctionalityImpl(HttpFunctionality httpFunc,
                                     ExecutorService exchangeExecutorService,
                                     int ttlCheckInterval,
                                     int exchangeTtl
    ) {

        if (httpFunc == null) {
            throw new NullPointerException("Parameter 'restFunc' is nul");
        }

        if (exchangeExecutorService == null) {
            throw new NullPointerException("Parameter 'exchangeExecutorService' is nul");
        }

        if (ttlCheckInterval <= 0) {
            throw new IllegalArgumentException("ttlCheckInterval is <= 0: " + ttlCheckInterval);
        }

        if (exchangeTtl <= 0) {
            throw new IllegalArgumentException("exchangeTtl is <= 0: " + ttlCheckInterval);
        }

        mHttpFunc = httpFunc;
        mExchangeExecutorService = exchangeExecutorService;
        mTtlCheckInterval = ttlCheckInterval;
        mExchangeTtl = exchangeTtl;
    }


    private static ExecutorService createDefaultExecutorService() {
        return Executors.newFixedThreadPool(DEFAULT_EXECUTOR_SERVICE_THREADS);
    }


    /**
     * Executes the exchange with the this instance's default exchange TTL
     *
     * @param exchange Exchange to be executed
     * @param xId      use {@link ExchangeFunctionality#generateXId()} first to get an ID and provide it here.
     *                 if you don't care about the id, provide null.
     * @throws RejectedExecutionException
     */
    @Override
    public void executeExchange(final Exchange<T> exchange, final Long xId) {
        executeExchange(exchange, xId, mExchangeTtl);
    }


    /**
     * @param exchange Exchange to be executed
     * @param xId      use {@link ExchangeFunctionality#generateXId()} first to get an ID and provide it here.
     *                 if you don't care about the id, provide null.
     * @param ttl      Time to live. If exceeded exchange will be automatically cancelled and will return with error
     * @throws RejectedExecutionException if ExchangeFunctionality is not started ({@link #start()} not called) or shutdown.
     * @see #start()
     * @see #shutdown()
     */
    public void executeExchange(final Exchange<T> exchange, final Long xId, long ttl) {
        if (mIsStarted) {
            if (!mIsShutdown) {
                if (!exchange.isCancelled() && !exchange.isExecuted()) {
                    final Long actualXId;
                    if (xId != null) {
                        actualXId = xId;
                    } else {
                        actualXId = generateXId();
                    }
                    mExchangesInFlight.put(actualXId, new InFlightTtlHelper(actualXId, getTime(), exchange, ttl));
                    mExchangeExecutorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                T result = exchange.execute(mHttpFunc);
                                if (result != null) {
                                    onExchangeResult(exchange, result, actualXId);
                                }
                            } catch (IOException e) {
                                onExchangeError(exchange, actualXId);
                            }
                        }
                    });
                }
            } else {
                throw new RejectedExecutionException("Already shutdown");
            }
        } else {
            throw new RejectedExecutionException("You forgot to call start() before this execute()");
        }
    }


    /**
     * Generates unique ID for the exchange
     * <p/>
     * This implementation uses AtomicLong for the ID generator in order to ensure thread safe behaviour
     *
     * @return Generated ID
     */
    @Override
    public Long generateXId() {
        if (!mIsStarted) {
            mLogger.warn("You need to call start() before reserving id exchange");
        }
        return mSequenceGenerator.incrementAndGet();
    }


    @Override
    public void start() {
        mLogger.trace("ExchangeFunctionalityImpl {} started", this);
        mTtlChecker = mScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                checkAndRemoveTtled();
            }
        }, mTtlCheckInterval, mTtlCheckInterval, TimeUnit.MILLISECONDS);
        mIsStarted = true;
    }


    @Override
    public void addListener(Listener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }


    @Override
    public void removeListener(Listener listener) {
        mListeners.remove(listener);
    }


    @Override
    public void shutdown() {
        if (mTtlChecker != null) {
            mTtlChecker.cancel(true);
        }

        mScheduler.shutdown();

        mExchangeExecutorService.shutdown();
        mIsShutdown = true;
    }


    @Override
    public boolean isStarted() {
        return mIsStarted;
    }


    @Override
    public boolean isShutdown() {
        return mIsShutdown;
    }


    @ForUnitTestsOnly
    List<Listener> getListeners() {
        return mListeners;
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        if (!mIsShutdown) {
            mLogger.warn("Seems you forgot to shutdown ExchangeFunctionalityImpl " + this);
            shutdown();
        }
    }


    protected void onExchangeResult(Exchange<T> x,
                                    T result,
                                  Long idL
    ) {
        ExchangeOutcome<T> outcome = new ExchangeOutcome<>(x, result, false);
        onExchangeCompleted(outcome, idL);
    }


    private void onExchangeError(Exchange<T> x, Long idL) {
        ExchangeOutcome<T> outcome = new ExchangeOutcome<>(x, null, true);
        onExchangeCompleted(outcome, idL);
    }


    private synchronized void onExchangeCompleted(ExchangeOutcome<T> outcome,
                                                  Long idL
    ) {
        mExchangesInFlight.remove(idL);
        for (Listener l : mListeners) {
            l.onExchangeCompleted(outcome, idL);
        }
    }


    private long getTime() {
        return System.nanoTime() / 1000000;
    }


    private void checkAndRemoveTtled() {
        for (InFlightTtlHelper hlp : mExchangesInFlight.values()) {
            if (hlp.mStartedAt + hlp.mTtl < getTime()) {
                mLogger.debug("Exchange {} TTLed", hlp.mXId);
                hlp.mExchange.cancel();
                onExchangeError(hlp.mExchange, hlp.mXId);
            }
        }
    }


    private static class InFlightTtlHelper<T> {
        final long mXId;
        final long mStartedAt;
        final Exchange<T> mExchange;
        final long mTtl;


        public InFlightTtlHelper(long XId,
                                 long startedAt,
                                 Exchange<T> exchange,
                                 long ttl
        ) {
            mXId = XId;
            mStartedAt = startedAt;
            mExchange = exchange;
            mTtl = ttl;
        }
    }


    /**
     * Builder for ExchangeFunctionalityImpl
     */
    public static class Builder {
        private final HttpFunctionality mHttpFunc;
        private int mTtlCheckInterval = TTL_CHECK_INTERVAL;
        private int mExchangeTtl = DEFAULT_EXCHANGE_TTL;
        private ExecutorService mExchangeExecutorService;


        private Builder(HttpFunctionality httpFunc) {
            mHttpFunc = httpFunc;
        }


        /**
         * Creates a new builder
         *
         * @param httpFunc RestFunctionality implementation
         * @return The new builder
         */
        public static Builder create(HttpFunctionality httpFunc) {
            return new Builder(httpFunc);
        }


        /**
         * Builds the {@link ExchangeFunctionalityImpl}
         *
         * @return The new ExchangeFunctionalityImpl
         */
        public ExchangeFunctionalityImpl build() {
            if (mExchangeExecutorService == null) {
                mExchangeExecutorService = createDefaultExecutorService();
            }
            return new ExchangeFunctionalityImpl(mHttpFunc,
                    mExchangeExecutorService,
                    mTtlCheckInterval,
                    mExchangeTtl);
        }


        /**
         * Sets the executor service
         *
         * @param service ExecutorService to be used
         * @return the builder itself
         */
        public Builder executorService(ExecutorService service) {
            mExchangeExecutorService = service;
            return this;
        }


        /**
         * Sets the TTL check interval
         *
         * @param ttlCheckInterval Interval between checks for TTLed exchanges
         * @return the builder itself
         */
        public Builder ttlCheckInterval(int ttlCheckInterval) {
            if (ttlCheckInterval <= 0) {
                throw new IllegalArgumentException("ttlCheckInterval must be grater than zero");
            }
            mTtlCheckInterval = ttlCheckInterval;

            return this;
        }


        /**
         * Sets the default TTL for exchanges. If you need custom TTL for given exchange
         * you can use {@link ExchangeFunctionality#(Exchange, Long, long)}
         *
         * @param exchangeTtl set default TTL for the exchanges
         * @return the builder itself
         */
        public Builder exchangeTtl(int exchangeTtl) {
            if (exchangeTtl <= 0) {
                throw new IllegalArgumentException("exchangeTtl must be grater than zero");
            }
            mExchangeTtl = exchangeTtl;

            return this;
        }
    }
}
