package com.bolyartech.forge.base.exchange.forge;

import com.bolyartech.forge.base.exchange.HttpExchange;
import com.bolyartech.forge.base.exchange.ResultProducer;
import com.bolyartech.forge.base.http.HttpFunctionality;
import okhttp3.Request;

public class ForgeHttpExchange extends HttpExchange<ForgeExchangeResult> {
    @SuppressWarnings("unused")
    public ForgeHttpExchange(HttpFunctionality httpFunctionality, Request request, ResultProducer<ForgeExchangeResult> resultProducer, Object tag) {
        super(httpFunctionality, request, resultProducer, tag);
    }
}
