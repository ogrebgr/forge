package com.bolyartech.forge.rest;


public interface KhRestFunctionality {
    /**
     * Starts the KhRestFunctionality
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
     * Executes {@link RestExchange} with default TTL
     *
     * @param x   RestExchange
     * @param xId ID of the exchange (use {@link #generateXId()} to generate ID
     */
    void executeKhRestExchange(final RestExchange<KhRestResult> x, Long xId);

    /**
     * Executes {@link RestExchange} with specified TTL
     *
     * @param x   RestExchange
     * @param xId ID of the exchange (use {@link #generateXId()} to generate ID
     * @param ttl TTL for the exchange
     */
    void executeKhRestExchange(final RestExchange<KhRestResult> x, Long xId, long ttl);

    /**
     * Generates unique ID for the exchange. Implementations of KhRestFunctionality
     * must provide thread safe implementation of this method.
     *
     * @return Generated ID
     */
    Long generateXId();

    /**
     * Shutdowns the KhRestFunctionality
     */
    void shutdown();

    /**
     * Checks if this KhRestFunctionality is started
     *
     * @return <code>true</code> if started, <code>false</code> otherwise
     */
    boolean isStarted();

    /**
     * Checks if this KhRestFunctionality is shutdown
     *
     * @return <code>true</code> if shutdown, <code>false</code> otherwise
     */
    boolean isShutdown();

    /**
     * Listener for completed exchanges
     */
    interface Listener {
        /**
         * Called then exchange is completed (either successful or not)
         *
         * @param out Outcome of the exchange
         * @param idL ID of the exchange
         */
        void onKhRestExchangeCompleted(RestExchangeOutcome<KhRestResult> out, long idL);
    }
}
