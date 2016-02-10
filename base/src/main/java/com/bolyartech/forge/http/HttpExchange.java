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

package com.bolyartech.forge.http;


import com.bolyartech.forge.exchange.Exchange;
import com.bolyartech.forge.exchange.ResultProducer;
import com.bolyartech.forge.http.functionality.HttpFunctionality;
import com.bolyartech.forge.http.misc.BasicResponseHandlerImpl;
import forge.apache.http.client.ResponseHandler;
import forge.apache.http.client.methods.HttpUriRequest;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Implementation of Exchange
 *
 * @param <T> Type of the produced object/result
 */
public class HttpExchange<T> implements Exchange<T> {
    private final HttpFunctionality mHttpFunctionality;
    private final HttpUriRequest mRequest;
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
    public HttpExchange(HttpFunctionality httpFunctionality, HttpUriRequest request, ResultProducer<T> resultProducer, Object tag) {
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
     * @param json        JsonFunctionality to be used to transform the returned JSON into object of type <code>T</code>
     */
    @SuppressWarnings("unused")
    public HttpExchange(HttpFunctionality httpFunctionality, HttpUriRequest request, ResultProducer<T> json) {
        this(httpFunctionality, request, json, null);
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
                ResponseHandler<String> responseHandler = new BasicResponseHandlerImpl();
                mLogger.debug("Executing: " + mRequest.getURI().toString());
                String rawResponse = mHttpFunctionality.execute(mRequest, responseHandler);
                mLogger.trace(rawResponse);
                T ret;
                try {
                    if (!mIsCancelled) {
                        ret = createResult(rawResponse);
                    } else {
                        return null;
                    }
                } catch (ResultProducer.ResultProducerException e) {
                    mLogger.error("ResultProducerException: " + rawResponse);
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
        if (!mIsExecuted) {
            mIsCancelled = true;
            mRequest.abort();
            mLogger.debug("cancel {}", this);
        }
    }


    @Override
    public boolean isCancelled() {
        return mIsCancelled;
    }


    /**
     * @param source String that contains JSON code
     * @return Object of type <code>T</code>
     * @throws ResultProducer.ResultProducerException if <code>source</code> is not valid JSON string
     */
    protected T createResult(String source) throws ResultProducer.ResultProducerException {
        try {
            return mResultProducer.produce(source);
        } catch (ResultProducer.ResultProducerException e) {
            mLogger.error("Json parse error: {}", e.getCause());
            mLogger.error(source.substring(0, source.length() < 1000 ? source.length() : 1000));
            throw e;
        }
    }
}
