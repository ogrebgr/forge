package com.bolyartech.forge.base.exchange.builders;

import com.bolyartech.forge.base.exchange.ResultProducer;
import com.bolyartech.forge.base.exchange.forge.ForgeExchangeResult;

import okhttp3.OkHttpClient;


/**
 * Forge POST HTTP exchange builder
 */
public class ForgePostHttpExchangeBuilder extends FormHttpExchangeBuilder<ForgeExchangeResult> {
    /**
     * Creates new ForgePostHttpExchangeBuilder
     * @param okHttpClient OkHttpClient to be used
     * @param resultProducer Result producer
     * @param url Url of the endpoint
     */
    @SuppressWarnings("unused")
    public ForgePostHttpExchangeBuilder(OkHttpClient okHttpClient,
                                        ResultProducer<ForgeExchangeResult> resultProducer,
                                        String url) {
        super(okHttpClient, resultProducer, url);
    }
}
