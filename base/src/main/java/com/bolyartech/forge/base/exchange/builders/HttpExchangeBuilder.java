package com.bolyartech.forge.base.exchange.builders;

import com.bolyartech.forge.base.exchange.ResultProducer;
import com.bolyartech.forge.base.exchange.HttpExchange;
import com.bolyartech.forge.base.http.HttpFunctionality;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * HTTP exchange builder
 * @param <T> Type of the result producer
 */
abstract public class HttpExchangeBuilder<T> {
    private final HttpFunctionality mHttpFunctionality;
    private final ResultProducer<T> mResultProducer;
    private final String mUrl;

    private final Map<String, String> mGetParams = new HashMap<>();


    /**
     * Builds the exchange
     * @return Exchange ready to be executed
     */
    @SuppressWarnings("unused")
    abstract public HttpExchange<T> build();


    /**
     * Creates new HttpExchangeBuilder
     * @param httpFunctionality HTTP functionality to be used
     * @param resultProducer Result producer
     * @param url URL of the endpoint
     */
    public HttpExchangeBuilder(HttpFunctionality httpFunctionality, ResultProducer<T> resultProducer, String url) {
        mHttpFunctionality = httpFunctionality;
        mResultProducer = resultProducer;
        mUrl = url;
    }


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
