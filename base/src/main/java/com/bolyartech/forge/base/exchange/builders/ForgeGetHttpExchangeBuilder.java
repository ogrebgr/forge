package com.bolyartech.forge.base.exchange.builders;

import com.bolyartech.forge.base.exchange.forge.ForgeExchangeResult;
import com.bolyartech.forge.base.exchange.ResultProducer;
import com.bolyartech.forge.base.http.HttpFunctionality;


/**
 * Forge GET HTTP exchange builder
 */
public class ForgeGetHttpExchangeBuilder extends GetHttpExchangeBuilder<ForgeExchangeResult> {
    /**
     * Creates new ForgeGetHttpExchangeBuilder
     * @param httpFunctionality HTTP functionality
     * @param resultProducer Result producer
     * @param url URL for the exchange
     */
    public ForgeGetHttpExchangeBuilder(HttpFunctionality httpFunctionality,
                                       ResultProducer<ForgeExchangeResult> resultProducer,
                                       String url) {
        super(httpFunctionality, resultProducer, url);
    }
}
