package com.bolyartech.forge.base.exchange.builders;

import com.bolyartech.forge.base.exchange.HttpExchange;
import com.bolyartech.forge.base.exchange.ResultProducer;

import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;


/**
 * Builder for GET exchanges
 * @param <T>
 */
@SuppressWarnings("WeakerAccess")
public class GetHttpExchangeBuilder<T> extends HttpExchangeBuilder<T> {


    /**
     * Creates new GetHttpExchangeBuilder
     * @param okHttpClient OkHttpClient to be used
     * @param resultProducer Result producer
     * @param url URL of the endpoint
     */
    public GetHttpExchangeBuilder(OkHttpClient okHttpClient, ResultProducer<T> resultProducer, String url) {
        super(okHttpClient, resultProducer, url);
    }


    @Override
    public HttpExchange<T> build() {
        HttpUrl url = createFullUrl();


        Request.Builder b = new Request.Builder();
        b.get();
        b.url(url);
        for (String key : mHeaderParams.keySet()) {
            b.addHeader(key, mHeaderParams.get(key));
        }
        b.cacheControl(CacheControl.FORCE_NETWORK);

        return new HttpExchange<>(getHttpClient(),
                b.build(),
                getResultProducer());
    }


    protected HttpUrl createFullUrl() {
        HttpUrl urlParsed = HttpUrl.parse(getUrl());
        if (urlParsed == null) {
            throw new IllegalArgumentException("Invalid URL: " + getUrl());
        }
        HttpUrl.Builder urlBuilder = urlParsed.newBuilder();
        Map<String, String> map = getGetParameters();
        for (String key : map.keySet()) {
            urlBuilder.addQueryParameter(key, map.get(key));
        }

        return urlBuilder.build();
    }


    /**
     * Checks if header parameter is present
     *
     * @param name Name of the parameter
     * @return true if parameter is already added, false otherwise
     */
    public boolean isHeaderParameterPresent(String name) {
        return mHeaderParams.containsKey(name);
    }
}
