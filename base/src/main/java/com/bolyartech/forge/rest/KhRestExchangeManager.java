package com.bolyartech.forge.rest;

public interface KhRestExchangeManager extends KhRestFunctionality.Listener {
    /**
     * Generate unique ID for an exchange
     *
     * @return Generated ID
     */
    Long generateXId();

    /**
     * @param exchange RestExchange
     * @param xId      use {@link KhRestExchangeManager#generateXId()} first to get an ID and provide it here.
     *                 if you don't care about the id, provide null.
     */
    void executeKhRestExchange(final RestExchange<KhRestResult> exchange, Long xId);
}
