package com.bolyartech.forge.base.exchange.builders;

import com.bolyartech.forge.base.exchange.forge.ForgeExchangeResult;
import com.bolyartech.forge.base.exchange.ResultProducer;
import com.bolyartech.forge.base.http.HttpFunctionality;

public class ForgeGetHttpExchangeBuilder extends GetHttpExchangeBuilder<ForgeExchangeResult> {
    @SuppressWarnings("unused")
    public ForgeGetHttpExchangeBuilder(HttpFunctionality httpFunctionality, ResultProducer<ForgeExchangeResult> resultProducer, String url) {
        super(httpFunctionality, resultProducer, url);
    }
}
