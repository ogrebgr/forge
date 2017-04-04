package com.bolyartech.forge.base.exchange.builders;

import com.bolyartech.forge.base.exchange.ResultProducer;
import com.bolyartech.forge.base.exchange.forge.ForgeExchangeResult;

import okhttp3.OkHttpClient;


/**
 * Forge GET HTTP exchange builder
 */
public class ForgeGetHttpExchangeBuilder extends GetHttpExchangeBuilder<ForgeExchangeResult> {
    /**
     * Creates new ForgeGetHttpExchangeBuilder
     * @param okHttpClient OkHttpClient to be used
     * @param resultProducer Result producer
     * @param url URL for the exchange
     */
    public ForgeGetHttpExchangeBuilder(OkHttpClient okHttpClient,
                                       ResultProducer<ForgeExchangeResult> resultProducer,
                                       String url) {
        super(okHttpClient, resultProducer, url);
    }
}
