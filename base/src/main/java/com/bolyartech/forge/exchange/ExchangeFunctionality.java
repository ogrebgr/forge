package com.bolyartech.forge.exchange;


public interface ExchangeFunctionality<T> {
    /**
     * Starts the ExchangeFunctionality
     */
    void start();

    /**
     * Adds a listener
     *
     * @param listener Listener to be added
     */
    void addListener(Listener listener);

    /**
     * Removes a listener
     *
     * @param listener Listener to be removed
     */
    void removeListener(Listener listener);

    /**
     * Executes {@link Exchange} with default TTL
     *
     * @param x   Exchange
     * @param xId ID of the exchange (use {@link #generateXId()} to generate ID
     */
    void executeExchange(final Exchange<T> x, Long xId);

    /**
     * Executes {@link Exchange} with specified TTL
     *
     * @param x   Exchange
     * @param xId ID of the exchange (use {@link #generateXId()} to generate ID
     * @param ttl TTL for the exchange
     */
    void executeExchange(final Exchange<T> x, Long xId, long ttl);

    /**
     * Generates unique ID for the exchange. Implementations of ExchangeFunctionality
     * must provide thread safe implementation of this method.
     *
     * @return Generated ID
     */
    Long generateXId();

    /**
     * Shutdowns the ExchangeFunctionality
     */
    void shutdown();

    /**
     * Checks if this ExchangeFunctionality is started
     *
     * @return <code>true</code> if started, <code>false</code> otherwise
     */
    boolean isStarted();

    /**
     * Checks if this ExchangeFunctionality is shutdown
     *
     * @return <code>true</code> if shutdown, <code>false</code> otherwise
     */
    boolean isShutdown();

    /**
     * Listener for completed exchanges
     */
    interface Listener<T> {
        /**
         * Called then exchange is completed (either successful or not)
         *
         * @param out Outcome of the exchange
         * @param idL ID of the exchange
         */
        void onExchangeCompleted(ExchangeOutcome<T> out, long idL);
    }
}
