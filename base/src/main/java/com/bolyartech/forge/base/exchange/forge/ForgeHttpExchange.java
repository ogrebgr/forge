package com.bolyartech.forge.base.exchange.forge;

import com.bolyartech.forge.base.exchange.HttpExchange;
import com.bolyartech.forge.base.exchange.ResultProducer;

import okhttp3.OkHttpClient;
import okhttp3.Request;


/**
 * HTTP exchange that produces ForgeExchangeResult result
 *
 * {@see HttpExchange}
 */
public class ForgeHttpExchange extends HttpExchange<ForgeExchangeResult> {
    /**
     * Creates new ForgeHttpExchange
     * @param okHttpClient OkHttpClient to be used
     * @param request Request to be send
     * @param resultProducer Result producer
     * @param tag Tag to be used. This is some object that you use to distinguish/identify different exchanges
     */
    @SuppressWarnings("unused")
    public ForgeHttpExchange(OkHttpClient okHttpClient,
                             Request request,
                             ResultProducer<ForgeExchangeResult> resultProducer,
                             Object tag) {
        super(okHttpClient, request, resultProducer, tag);
    }


    /**
     * Creates new ForgeHttpExchange
     * @param okHttpClient OkHttpClient to be used
     * @param request Request to be send
     * @param resultProducer Result producer
     */
    @SuppressWarnings("unused")
    public ForgeHttpExchange(OkHttpClient okHttpClient,
                             Request request,
                             ResultProducer<ForgeExchangeResult>
                                     resultProducer) {
        this(okHttpClient, request, resultProducer, null);
    }
}
