/*
 * Copyright (C) 2012-2015 Ognyan Bankov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bolyartech.forge.exchange;

import com.bolyartech.forge.http.functionality.HttpFunctionality;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * Created by ogre on 2015-11-01
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
    protected void onExchangeResult(Exchange<ForgeExchangeResult> x, ForgeExchangeResult result, Long exchangeId) {
        if (result.getCode() > 0) {
            mLogger.debug("Exchange {} returned with code {}", x.getClass().getSimpleName(), result.getCode());
        } else {
            mLogger.warn("Exchange {} returned with code {}", x.getClass().getSimpleName(), result.getCode());
        }

        super.onExchangeResult(x, result, exchangeId);
    }
}
