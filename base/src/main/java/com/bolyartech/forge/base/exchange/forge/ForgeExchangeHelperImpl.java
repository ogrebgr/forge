package com.bolyartech.forge.base.exchange.forge;

import com.bolyartech.forge.base.exchange.ForgeExchangeManager;
import com.bolyartech.forge.base.exchange.ResultProducer;
import com.bolyartech.forge.base.exchange.builders.ForgeGetHttpExchangeBuilder;
import com.bolyartech.forge.base.exchange.builders.ForgePostHttpExchangeBuilder;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.OkHttpClient;


/**
 * Helper class for creating Forge exchange builders
 */
public class ForgeExchangeHelperImpl implements ForgeExchangeHelper {
    private final ForgeExchangeManager mExchangeManager;

    private final OkHttpClient mOkHttpClient;

    private final ResultProducer<ForgeExchangeResult> mResultProducer;

    private final String mBaseUrl;


    /**
     * Creates new ForgeExchangeHelperImpl
     * @param exchangeManager Exchange manager to be used
     * @param okHttpClient OkHttpClient to be used
     * @param resultProducer Result producer
     * @param baseUrl Base url which will be used as a prefix to endpoint
     */
    @SuppressWarnings("unused")
    @Inject
    public ForgeExchangeHelperImpl(ForgeExchangeManager exchangeManager,
                                   OkHttpClient okHttpClient,
                                   @Named("forge result producer") ResultProducer<ForgeExchangeResult> resultProducer,
                                   @Named("base url") String baseUrl) {

        mExchangeManager = exchangeManager;
        mOkHttpClient = okHttpClient;
        mResultProducer = resultProducer;
        mBaseUrl = baseUrl;
    }


    @Override
    public ForgePostHttpExchangeBuilder createForgePostHttpExchangeBuilder(String endpoint) {
        return new ForgePostHttpExchangeBuilder(mOkHttpClient, mResultProducer, mBaseUrl + endpoint);
    }


    @Override
    public ForgeGetHttpExchangeBuilder createForgeGetHttpExchangeBuilder(String endpoint) {
        return new ForgeGetHttpExchangeBuilder(mOkHttpClient, mResultProducer, mBaseUrl + endpoint);
    }


    @Override
    public ForgeExchangeManager getExchangeManager() {
        return mExchangeManager;
    }
}
