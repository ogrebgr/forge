package com.bolyartech.forge.exchange;

import com.bolyartech.forge.http.functionality.HttpFunctionality;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * Created by ogre on 27.10.15.
 */
public class ForgeExchangeFunctionality extends ExchangeFunctionalityImpl<ForgeExchangeResult> {
    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(ExchangeFunctionalityImpl.class
            .getSimpleName());


    public ForgeExchangeFunctionality(HttpFunctionality httpFunc) {
        super(httpFunc);
    }


    public ForgeExchangeFunctionality(HttpFunctionality httpFunc, ExecutorService exchangeExecutorService) {
        super(httpFunc, exchangeExecutorService);
    }


    public ForgeExchangeFunctionality(HttpFunctionality httpFunc, ExecutorService exchangeExecutorService, int ttlCheckInterval, int exchangeTtl) {
        super(httpFunc, exchangeExecutorService, ttlCheckInterval, exchangeTtl);
    }


    @Override
    protected void onExchangeResult(Exchange<ForgeExchangeResult> x, ForgeExchangeResult result, Long idL) {
        if (result.getCode() > 0) {
            mLogger.debug("Exchange {} returned with code {}", x.getClass().getSimpleName(), result.getCode());
        } else {
            mLogger.warn("Exchange {} returned with code {}", x.getClass().getSimpleName(), result.getCode());
        }

        super.onExchangeResult(x, result, idL);
    }
}
