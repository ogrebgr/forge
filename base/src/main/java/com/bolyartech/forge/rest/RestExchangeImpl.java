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
import com.bolyartech.forge.http.misc.KhandroidBasicResponseHandler;
import com.bolyartech.forge.misc.JsonFunctionality;
import forge.apache.http.client.ResponseHandler;
import forge.apache.http.client.methods.HttpUriRequest;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Implementation of RestExchange
 *
 * @param <T> Type of the produced object/result
 */
public class RestExchangeImpl<T> implements RestExchange<T> {
    private final HttpUriRequest mRequest;
    private final JsonFunctionality mJson;
    private final Class<T> mResultClass;
    private final Object mTag;
    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(RestExchangeImpl.class
            .getSimpleName());
    private volatile boolean mIsExecuted = false;
    private volatile boolean mIsCancelled = false;


    /**
     * Creates new RestExchangeImpl
     *
     * @param request     Request to be executed
     * @param json        JsonFunctionality to be used to transform the returned JSON into object of type <code>T</code>
     * @param resultClass Class of the result object
     * @param tag         Tag object
     */
    public RestExchangeImpl(HttpUriRequest request, JsonFunctionality json, Class<T> resultClass, Object tag) {
        super();
        if (request == null) {
            throw new NullPointerException("Parameter 'request' is nul");
        }

        if (json == null) {
            throw new NullPointerException("Parameter 'json' is nul");
        }

        if (resultClass == null) {
            throw new NullPointerException("Parameter 'resultClass' is nul");
        }

        mRequest = request;
        mJson = json;
        mResultClass = resultClass;
        mTag = tag;
    }


    /**
     * Creates new RestExchangeImpl
     *
     * @param request     Request to be executed
     * @param json        JsonFunctionality to be used to transform the returned JSON into object of type <code>T</code>
     * @param resultClass Class of the result object
     */
    public RestExchangeImpl(HttpUriRequest request, JsonFunctionality json, Class<T> resultClass) {
        this(request, json, resultClass, null);
    }


    /**
     * Executes the exchange.
     *
     * @param mHttpFunc HttpFunctionlaity implementation
     * @return Returns <code>T</code> object or <code>null</code> if exchange was cancelled.
     * @throws IOException                          if network error occurs
     * @throws JsonFunctionality.JsonParseException if the String result of the http request is not valid JSON string
     */
    public synchronized T execute(HttpFunctionality mHttpFunc) throws
            IOException, JsonFunctionality.JsonParseException {
        if (!mIsCancelled) {
            if (!mIsExecuted) {
                ResponseHandler<String> responseHandler = new KhandroidBasicResponseHandler();
                mLogger.debug("Executing: " + mRequest.getURI().toString());
                String rawResponse = mHttpFunc.execute(mRequest, responseHandler);
                mLogger.trace(rawResponse);
                T ret;
                try {
                    mLogger.debug("is cancelled {}", this);
                    if (!mIsCancelled) {
                        ret = createResult(rawResponse);
                    } else {
                        return null;
                    }
                } catch (JsonFunctionality.JsonParseException e) {
                    mLogger.error("JsonParseException: " + rawResponse);
                    throw e;
                }
                mLogger.trace(ret.toString());

                mIsExecuted = true;
                return ret;
            } else {
                throw new IllegalStateException("Already executed");
            }
        } else {
            return null;
        }
    }


    public Object getTag() {
        return mTag;
    }


    @Override
    public boolean isExecuted() {
        return mIsExecuted;
    }


    @Override
    public void cancel() {
        mIsCancelled = true;
        mLogger.debug("cancel {}", this);
    }


    @Override
    public boolean isCancelled() {
        return mIsCancelled;
    }


    /**
     * @param source String that contains JSON code
     * @return Object of type <code>T</code>
     * @throws JsonFunctionality.JsonParseException if <code>source</code> is not valid JSON string
     */
    protected T createResult(String source) throws JsonFunctionality.JsonParseException {
        try {
            return mJson.fromJson(source, mResultClass);
        } catch (JsonFunctionality.JsonParseException e) {
            mLogger.error("Json parse error: {}", e.getCause());
            mLogger.error(source.substring(0, source.length() < 1000 ? source.length() : 1000));
            throw e;
        }
    }
}
