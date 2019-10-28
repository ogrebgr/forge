package com.bolyartech.forge.base.exchange.builders;

import com.bolyartech.forge.base.exchange.HttpExchange;
import com.bolyartech.forge.base.exchange.ResultProducer;

import okhttp3.CacheControl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class PostJsonBodyHttpExchangeBuilder<T> extends HttpExchangeBuilder<T> {
    private String body;


    /**
     * Creates new PostHttpExchangeBuilder
     *
     * @param okHttpClient   OkHttpClient to be used
     * @param resultProducer Result producer
     * @param url            URL of the endpoint
     */
    public PostJsonBodyHttpExchangeBuilder(OkHttpClient okHttpClient, ResultProducer<T> resultProducer, String url) {
        super(okHttpClient, resultProducer, url);
    }


    public PostJsonBodyHttpExchangeBuilder<T> body(String body) {
        this.body = body;
        return this;
    }


    @Override
    public HttpExchange<T> build() {
        if (body == null) {
            throw new IllegalStateException("Body not initialized. You forgot to call body()");
        }

        Request.Builder b = new Request.Builder();
        b.post(RequestBody.create(body, MediaType.get("application/json")));
        b.url(getUrl());
        for (String key : mHeaderParams.keySet()) {
            b.addHeader(key, mHeaderParams.get(key));
        }
        b.cacheControl(CacheControl.FORCE_NETWORK);

        return new HttpExchange<>(getHttpClient(),
                b.build(),
                getResultProducer());
    }
}
