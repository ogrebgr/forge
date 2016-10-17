package com.bolyartech.forge.base.exchange.forge;

import com.bolyartech.forge.base.exchange.*;
import com.bolyartech.forge.base.exchange.builders.ForgeGetHttpExchangeBuilder;
import com.bolyartech.forge.base.exchange.builders.ForgePostHttpExchangeBuilder;
import com.bolyartech.forge.base.http.HttpFunctionality;

import javax.inject.Inject;
import javax.inject.Named;


/**
 * Helper class for creating Forge exchange builders
 */
public class ForgeExchangeHelperImpl implements ForgeExchangeHelper {
    private final ForgeExchangeManager mExchangeManager;

    private final HttpFunctionality mHttpFunctionality;

    private final ResultProducer<ForgeExchangeResult> mResultProducer;

    private final String mBaseUrl;


    /**
     * Creates new ForgeExchangeHelperImpl
     * @param exchangeManager Exchange manager to be used
     * @param httpFunctionality HTTP functionality
     * @param resultProducer Result producer
     * @param baseUrl Base url which will be used as a prefix to endpoint
     */
    @SuppressWarnings("unused")
    @Inject
    public ForgeExchangeHelperImpl(ForgeExchangeManager exchangeManager,
                                   HttpFunctionality httpFunctionality,
                                   @Named("forge result producer") ResultProducer<ForgeExchangeResult> resultProducer,
                                   @Named("base url") String baseUrl) {

        mExchangeManager = exchangeManager;
        mHttpFunctionality = httpFunctionality;
        mResultProducer = resultProducer;
        mBaseUrl = baseUrl;
    }


    @Override
    public ForgePostHttpExchangeBuilder createForgePostHttpExchangeBuilder(String endpoint) {
        return new ForgePostHttpExchangeBuilder(mHttpFunctionality, mResultProducer, mBaseUrl + endpoint);
    }


    @Override
    public ForgeGetHttpExchangeBuilder createForgeGetHttpExchangeBuilder(String endpoint) {
        return new ForgeGetHttpExchangeBuilder(mHttpFunctionality, mResultProducer, mBaseUrl + endpoint);
    }


    @Override
    public ForgeExchangeManager getExchangeManager() {
        return mExchangeManager;
    }
}
