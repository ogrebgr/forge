package com.bolyartech.forge.base.exchange.builders;

import com.bolyartech.forge.base.exchange.ForgeExchangeResult;
import com.bolyartech.forge.base.exchange.ResultProducer;
import com.bolyartech.forge.base.http.HttpFunctionality;

public class ForgePostHttpExchangeBuilder extends FormHttpExchangeBuilder<ForgeExchangeResult> {
    public ForgePostHttpExchangeBuilder(HttpFunctionality httpFunctionality,
                                        ResultProducer<ForgeExchangeResult> resultProducer,
                                        String url) {
        super(httpFunctionality, resultProducer, url);
    }
}