package com.bolyartech.forge.base.exchange;

import com.bolyartech.forge.base.task.TaskExecutor;


/**
 * Facilitates execution of {Exchange}s
 * @param <T>
 * @deprecated
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
    long executeExchange(Exchange<T> x);


    /**
     * Executes the exchange with internally generated exchange ID and the default TTL
     * On outcome <b>ONLY</b> the ExchangeOutcomeHandler will be called but not the listeners
     * @param x Exchange to be executed
     * @param handler Handler to be called on outcome
     * @return generated exchange ID
     */
    long executeExchange(Exchange<T> x, ExchangeOutcomeHandler<T> handler);


    /**
     * Executes the exchange with internally generated exchange ID
     * On outcome <b>ONLY</b> the ExchangeOutcomeHandler will be called but not the listeners
     * @param x Exchange to be executed
     * @param handler Handler to be called on outcome
     * @param ttl TTL
     * @return generated exchange ID
     */
    long executeExchange(Exchange<T> x, ExchangeOutcomeHandler<T> handler, long ttl);


    /**
     * Cancel an exchange. You will not receive notification for the outcome of the cancelled exchanges
     * @param xId ID of the exchange
     */
    @SuppressWarnings("unused")
    void cancelExchange(Long xId);

    /**
     * @return true if started, false otherwise
     */
    @SuppressWarnings("unused")
    boolean isStarted();


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
     * Interface
     */
    interface ExchangeOutcomeHandler<T> {
        void handle(boolean isSuccess, T result);
    }
}
