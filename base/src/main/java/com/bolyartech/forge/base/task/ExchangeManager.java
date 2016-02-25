package com.bolyartech.forge.base.task;

import com.bolyartech.forge.base.exchange.Exchange;


/**
 * Created by ogre on 2016-01-12 13:36
 */
public interface ExchangeManager<T> {
    void start();
    void shutdown();

    void addListener(Listener<T> listener);

    void removeListener(Listener<T> listener);

    void executeExchange(Exchange<T> x);

    void executeExchange(Exchange<T> x, Long xId);

    void executeExchange(Exchange<T> x, Long xId, long ttl);

    void cancelExchange(Long xId);

    Long generateTaskId();

    interface Listener<T> {
        void onExchangeOutcome(long exchangeId, boolean isSuccess, T result);
    }
}
