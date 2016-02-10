package com.bolyartech.forge.exchange;

import com.bolyartech.forge.http.functionality.HttpFunctionality;

@SuppressWarnings("unused")
public class ForgeExchangeBuilderFactory {
    private final HttpFunctionality mHttpFunctionality;
    private final String mBaseUrl;
    private final ResultProducer<ForgeExchangeResult> mResultProducer;


    public ForgeExchangeBuilderFactory(HttpFunctionality httpFunctionality, String baseUrl, ResultProducer<ForgeExchangeResult> resultProducer) {
        mHttpFunctionality = httpFunctionality;
        mBaseUrl = baseUrl;
        mResultProducer = resultProducer;
    }


    public ForgeExchangeBuilder create(String endpoint) {
        if (endpoint == null) {
            throw new NullPointerException("endpoint is null");
        }
        return new ForgeExchangeBuilder(mHttpFunctionality, mBaseUrl, endpoint, mResultProducer);
    }
}
