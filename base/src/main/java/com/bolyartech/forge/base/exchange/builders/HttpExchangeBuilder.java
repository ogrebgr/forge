package com.bolyartech.forge.base.exchange.builders;

import com.bolyartech.forge.base.exchange.ResultProducer;
import com.bolyartech.forge.base.exchange.HttpExchange;
import com.bolyartech.forge.base.http.HttpFunctionality;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

abstract public class HttpExchangeBuilder<T> {
    private final HttpFunctionality mHttpFunctionality;
    private final ResultProducer<T> mResultProducer;
    private final String mUrl;

    private final Map<String, String> mGetParams = new HashMap<>();


    @SuppressWarnings("unused")
    abstract public HttpExchange<T> build();


    public HttpExchangeBuilder(HttpFunctionality httpFunctionality, ResultProducer<T> resultProducer, String url) {
        mHttpFunctionality = httpFunctionality;
        mResultProducer = resultProducer;
        mUrl = url;
    }


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


    @SuppressWarnings("unused")
    public boolean isGetParameterPresent(String name) {
        return mGetParams.containsKey(name);
    }


    protected Map<String, String> getGetParameters() {
        return mGetParams;
    }


    protected HttpFunctionality getHttpFunctionality() {
        return mHttpFunctionality;
    }


    protected ResultProducer<T> getResultProducer() {
        return mResultProducer;
    }


    protected String getUrl() {
        return mUrl;
    }
}
