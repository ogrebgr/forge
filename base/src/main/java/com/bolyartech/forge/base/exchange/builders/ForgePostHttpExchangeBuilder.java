package com.bolyartech.forge.base.exchange.builders;

import com.bolyartech.forge.base.exchange.forge.ForgeExchangeResult;
import com.bolyartech.forge.base.exchange.ResultProducer;
import com.bolyartech.forge.base.http.HttpFunctionality;


/**
 * Forge POST HTTP exchange builder
 */
public class ForgePostHttpExchangeBuilder extends FormHttpExchangeBuilder<ForgeExchangeResult> {
    /**
     * Creates new ForgePostHttpExchangeBuilder
     * @param httpFunctionality HTTP functionality to be used
     * @param resultProducer Result producer
     * @param url Url of the endpoint
     */
    @SuppressWarnings("unused")
    public ForgePostHttpExchangeBuilder(HttpFunctionality httpFunctionality,
                                        ResultProducer<ForgeExchangeResult> resultProducer,
                                        String url) {
        super(httpFunctionality, resultProducer, url);
    }
}
