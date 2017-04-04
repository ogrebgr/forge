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


import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ProtocolException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Implementation of Exchange
 *
 * @param <T> Type of the produced object/result
 */
@SuppressWarnings("WeakerAccess")
public class HttpExchange<T> implements Exchange<T> {
    private final OkHttpClient mOkHttpClient;
    private final Request mRequest;
    private final ResultProducer<T> mResultProducer;
    private final Object mTag;
    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(HttpExchange.class
            .getSimpleName());
    private volatile boolean mIsExecuted = false;
    private volatile boolean mIsCancelled = false;

    private volatile Call mCall;


    /**
     * Creates new HttpExchange
     * @param okHttpClient OkHttp client
     * @param request Request to be send
     * @param resultProducer Result producer to used
     * @param tag Tag to be used. This is some object that you use to distinguish/identify different exchanges
     */
    public HttpExchange(OkHttpClient okHttpClient,
                        Request request,
                        ResultProducer<T> resultProducer,
                        Object tag) {

        if (okHttpClient == null) {
            throw new NullPointerException("Parameter 'httpFunctionality' is null");
        }

        if (request == null) {
            throw new NullPointerException("Parameter 'request' is null");
        }

        if (resultProducer == null) {
            throw new NullPointerException("Parameter 'resultProducer' is null");
        }

        mOkHttpClient = okHttpClient;
        mRequest = request;
        mResultProducer = resultProducer;
        mTag = tag;
    }


    /**
     * Creates new HttpExchange
     * @param okHttpClient OkHttp client
     * @param request Request to be send
     * @param resultProducer Result producer to used
     */
    public HttpExchange(OkHttpClient okHttpClient, Request request, ResultProducer<T> resultProducer) {
        this(okHttpClient, request, resultProducer, null);
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

                mCall = mOkHttpClient.newCall(mRequest);
                Response resp = mCall.execute();
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
                    mLogger.warn(mRequest.url().toString() + " failed with code: " + resp.code());
                    throw new ProtocolException(mRequest.url().toString() + " failed with code: " + resp.code());
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
            if (mCall != null) {
                mCall.cancel();
            }
        }
    }


    @Override
    public boolean isCancelled() {
        return mIsCancelled;
    }
}
