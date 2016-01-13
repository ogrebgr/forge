package com.bolyartech.forge.task;

import com.bolyartech.forge.exchange.Exchange;
import com.bolyartech.forge.exchange.ExchangeOutcome;


/**
 * Created by ogre on 2016-01-12 13:36
 */
public interface ExchangeManager<T> {
    public void addListener(Listener<T> listener);

    public void removeListener(Listener<T> listener);

    public void executeExchange(Exchange<T> x);

    public void executeExchange(Exchange<T> x, Long xId);

    public void executeExchange(Exchange<T> x, Long xId, long ttl);

    public Long generateTaskId();

    interface Listener<T> {
        void onExchangeOutcome(long exchangeId, boolean isSuccess, T result);
    }
}
