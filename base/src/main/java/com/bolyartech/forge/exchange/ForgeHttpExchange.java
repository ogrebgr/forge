package com.bolyartech.forge.exchange;

import com.bolyartech.forge.http.functionality.HttpExchange;
import com.bolyartech.forge.http.functionality.HttpFunctionality;
import forge.apache.http.client.methods.HttpUriRequest;


public class ForgeHttpExchange extends HttpExchange<ForgeExchangeResult> {
    public ForgeHttpExchange(HttpFunctionality httpFunctionality, HttpUriRequest request, ResultProducer<ForgeExchangeResult> resultProducer, Class<ForgeExchangeResult> resultClass, Object tag) {
        super(httpFunctionality, request, resultProducer, resultClass, tag);
    }


    public ForgeHttpExchange(HttpFunctionality httpFunctionality, HttpUriRequest request, ResultProducer<ForgeExchangeResult> resultProducer, Class<ForgeExchangeResult> resultClass) {
        super(httpFunctionality, request, resultProducer, resultClass);
    }


}
