/*
 * Copyright (C) 2012-2016 Ognyan Bankov
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

package com.bolyartech.forge.base.exchange;


import com.bolyartech.forge.base.http.HttpFunctionality;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Implementation of Exchange
 *
 * @param <T> Type of the produced object/result
 */
public class HttpExchange<T> implements Exchange<T> {
    private final HttpFunctionality mHttpFunctionality;
    private final Request mRequest;
    private final ResultProducer<T> mResultProducer;
    private final Object mTag;
    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(HttpExchange.class
            .getSimpleName());
    private volatile boolean mIsExecuted = false;
    private volatile boolean mIsCancelled = false;


    /**
     * Creates new HttpExchange
     *
     * @param request     Request to be executed
     * @param resultProducer        JsonFunctionality to be used to transform the returned JSON into object of type <code>T</code>
     * @param tag         Tag object
     */
    public HttpExchange(HttpFunctionality httpFunctionality, Request request, ResultProducer<T> resultProducer, Object tag) {
        super();

        if (httpFunctionality == null) {
            throw new NullPointerException("Parameter 'httpFunctionality' is null");
        }

        if (request == null) {
            throw new NullPointerException("Parameter 'request' is null");
        }

        if (resultProducer == null) {
            throw new NullPointerException("Parameter 'resultProducer' is null");
        }

        mHttpFunctionality = httpFunctionality;
        mRequest = request;
        mResultProducer = resultProducer;
        mTag = tag;
    }


    /**
     * Creates new HttpExchange
     *
     * @param request     Request to be executed
     * @param resultProducer        JsonFunctionality to be used to transform the returned JSON into object of type <code>T</code>
     */
    @SuppressWarnings("unused")
    public HttpExchange(HttpFunctionality httpFunctionality, Request request, ResultProducer<T> resultProducer) {
        this(httpFunctionality, request, resultProducer, null);
    }


    /**
     * Executes the exchange.
     *
     * @return Returns <code>T</code> object or <code>null</code> if exchange was cancelled.
     * @throws IOException                          if network error occurs
     * @throws ResultProducer.ResultProducerException if the String result of the http request is not valid JSON string
     */
    @Override
    public synchronized T execute() throws
            IOException, ResultProducer.ResultProducerException {
        if (!mIsCancelled) {
            if (!mIsExecuted) {
                mLogger.debug("Executing: " + mRequest.url().toString());

                Response resp = mHttpFunctionality.execute(mRequest);
                T ret = null;
                if (resp.isSuccessful()) {
                    try {
                        if (!mIsCancelled) {
                            ret = mResultProducer.produce(resp);
                            mIsExecuted = true;
                        }
                    } catch (ResultProducer.ResultProducerException e) {
                        mLogger.error("ResultProducerException: {}", e);
                        throw e;
                    }
                } else {
                    mLogger.debug("mRequest.url().toString() failed with code: {}", resp.code());
                }

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
        if (!mIsExecuted) {
            mIsCancelled = true;
            mLogger.debug("cancel {}", this);
        }
    }


    @Override
    public boolean isCancelled() {
        return mIsCancelled;
    }
}
