package com.bolyartech.forge.base.exchange.builders;

import com.bolyartech.forge.base.exchange.HttpExchange;
import com.bolyartech.forge.base.exchange.ResultProducer;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;


/**
 * HTTP exchange builder
 * @param <T> Type of the result producer
 */
abstract public class HttpExchangeBuilder<T> {
    private final OkHttpClient mHttpClient;
    private final ResultProducer<T> mResultProducer;
    private final String mUrl;

    private final Map<String, String> mGetParams = new HashMap<>();


    /**
     * Creates new HttpExchangeBuilder
     * @param okHttpClient OkHttpClient to be used
     * @param resultProducer Result producer
     * @param url URL of the endpoint
     */
    public HttpExchangeBuilder(OkHttpClient okHttpClient, ResultProducer<T> resultProducer, String url) {
        mHttpClient = okHttpClient;
        mResultProducer = resultProducer;
        mUrl = url;
    }


    /**
     * Builds the exchange
     * @return Exchange ready to be executed
     */
    @SuppressWarnings("unused")
    abstract public HttpExchange<T> build();


    /**
     * Adds GET parameter
     * @param name Name of the parameter
     * @param value Value of the parameter
     */
    @SuppressWarnings("unused")
    public void addGetParameter(String name, String value) {
        if (value == null) {
            throw new NullPointerException("value == null");
        }

        if (mGetParams.containsKey(name)) {
            throw new IllegalStateException(MessageFormat.format("GET Parameter '{0}' already added.", name));
        }

        mGetParams.put(name, value);
    }


    /**
     * Checks if GET parameter is present
     * @param name Name of the parameter
     * @return true if parameter is already added, false otherwise
     */
    @SuppressWarnings("unused")
    public boolean isGetParameterPresent(String name) {
        return mGetParams.containsKey(name);
    }


    protected Map<String, String> getGetParameters() {
        return mGetParams;
    }


    protected OkHttpClient getHttpClient() {
        return mHttpClient;
    }


    protected ResultProducer<T> getResultProducer() {
        return mResultProducer;
    }


    protected String getUrl() {
        return mUrl;
    }
}
