package com.bolyartech.forge.base.task;

import com.bolyartech.forge.base.exchange.Exchange;


/**
 * Facilitates execution of {Exchange}s
 * @param <T>
 */
public interface ExchangeManager<T> {
    /**
     *
     * @param taskExecutor task executor to be used
     */
    @SuppressWarnings("unused")
    void start(TaskExecutor<T> taskExecutor);

    /**
     * Stops the exchange manager
     */
    @SuppressWarnings("unused")
    void shutdown();

    /**
     * Adds a listener
     * @param listener Listener to be added
     */
    @SuppressWarnings("unused")
    void addListener(Listener<T> listener);

    /**
     * Removes a listener
     * @param listener Listener to be removed
     */
    @SuppressWarnings("unused")
    void removeListener(Listener<T> listener);

    /**
     * Executes the exchange with internally generated exchange ID and the default TTL
     * @param x Exchange to be executed
     */
    @SuppressWarnings("unused")
    void executeExchange(Exchange<T> x);

    /**
     * Executes the exchange with the default TTL
     * @param x Exchange to be executed
     * @param xId ID of the exchange
     */
    @SuppressWarnings("unused")
    void executeExchange(Exchange<T> x, Long xId);

    /**
     * Executes the exchange
     * @param x Exchange to be executed
     * @param xId ID of the exchange
     * @param ttl TTL
     */
    @SuppressWarnings("unused")
    void executeExchange(Exchange<T> x, Long xId, long ttl);

    /**
     * Cancel an exchange. You will not receive notification for the outcome of the cancelled exchanges
     * @param xId ID of the exchange
     */
    @SuppressWarnings("unused")
    void cancelExchange(Long xId);

    /**
     * Generates unique ID to be used as exchange ID
     * @return Generated ID
     */
    @SuppressWarnings("unused")
    Long generateTaskId();

    /**
     * Listener for ExchangeManager
     * @param <T>
     */
    interface Listener<T> {
        /**
         * Called when exchange is completed either successfully or not
         *
         * @param exchangeId ID of the exchange
         * @param isSuccess True if exchange completed successfully
         * @param result Result of the exchange (if any)
         */
        void onExchangeOutcome(long exchangeId, boolean isSuccess, T result);
    }


    /**
     * @return true if started, false otherwise
     */
    @SuppressWarnings("unused")
    boolean isStarted();
}
