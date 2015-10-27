package com.bolyartech.forge.exchange;

public interface ExchangeManager<T> extends ExchangeFunctionality.Listener {
    /**
     * Generate unique ID for an exchange
     *
     * @return Generated ID
     */
    Long generateXId();

    /**
     * @param exchange Exchange
     * @param xId      use {@link ExchangeManager#generateXId()} first to get an ID and provide it here.
     *                 if you don't care about the id, provide null.
     */
    void executeExchange(final Exchange<T> exchange, Long xId);
}
