package com.bolyartech.forge.base.exchange;

import com.bolyartech.forge.base.http.HttpFunctionality;
import okhttp3.Request;

public class ForgeHttpExchange extends HttpExchange<ForgeExchangeResult> {
    public ForgeHttpExchange(HttpFunctionality httpFunctionality, Request request, ResultProducer<ForgeExchangeResult> resultProducer, Object tag) {
        super(httpFunctionality, request, resultProducer, tag);
    }
}
