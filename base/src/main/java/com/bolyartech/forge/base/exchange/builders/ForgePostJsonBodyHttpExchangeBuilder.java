package com.bolyartech.forge.base.exchange.builders;

import com.bolyartech.forge.base.exchange.ResultProducer;
import com.bolyartech.forge.base.exchange.forge.ForgeExchangeResult;

import okhttp3.OkHttpClient;


public class ForgePostJsonBodyHttpExchangeBuilder extends PostJsonBodyHttpExchangeBuilder<ForgeExchangeResult> {

    /**
     * Creates new PostHttpExchangeBuilder
     *
     * @param okHttpClient   OkHttpClient to be used
     * @param resultProducer Result producer
     * @param url            URL of the endpoint
     */
    public ForgePostJsonBodyHttpExchangeBuilder(OkHttpClient okHttpClient, ResultProducer resultProducer, String url) {
        super(okHttpClient, resultProducer, url);
    }
}
