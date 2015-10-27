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

package com.bolyartech.forge.rest;


import com.bolyartech.forge.http.functionality.HttpFunctionality;
import com.bolyartech.forge.misc.JsonFunctionality;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.UnknownHostException;


/**
 * RestFunctionality implementation
 */
public class RestFunctionalityImpl implements RestFunctionality {
    private final HttpFunctionality mHttpFunc;
    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(this.getClass()
            .getSimpleName());


    /**
     * Creates new HttpFunctionality
     *
     * @param httpFunc HttpFunctionality that will be used to execute Rest exchanges
     */
    public RestFunctionalityImpl(HttpFunctionality httpFunc) {
        if (httpFunc != null) {
            mHttpFunc = httpFunc;
        } else {
            throw new NullPointerException("Parameter 'httpFunc' is null");
        }
    }


    @Override
    public <T> T execute(RestExchange<T> x) throws RestExchangeFailedException {

        try {
            try {
                return x.execute(mHttpFunc);
            } catch (JsonFunctionality.JsonParseException e) {
                mLogger.error("JsonParseException: " + e.getMessage());
                throw new RestExchangeFailedException("Parsing response failed because of malformed json returned from server.", e);
            }
        } catch (forge.apache.http.client.ClientProtocolException e) {
            mLogger.error("ClientProtocolException: " + e.getMessage());
            throw new RestExchangeFailedException("Executing request failed because of protocol violation.", e);
        } catch (UnknownHostException e) {
            mLogger.error("UnknownHostException: " + e.getMessage());
            throw new RestExchangeFailedException("Executing request failed. Unknown host. Is there internet connection?", e);
        } catch (IOException e) {
            mLogger.error("IOException: " + e.getMessage());
            throw new RestExchangeFailedException("Executing request failed.", e);
        }
    }
}
