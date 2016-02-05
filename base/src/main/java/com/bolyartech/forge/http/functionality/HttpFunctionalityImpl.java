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

package com.bolyartech.forge.http.functionality;

import com.bolyartech.forge.http.ForgeCloseableHttpClient;
import com.bolyartech.forge.http.misc.BasicResponseHandlerImpl;
import forge.apache.http.HttpResponse;
import forge.apache.http.client.ResponseHandler;
import forge.apache.http.client.methods.HttpUriRequest;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Implementation of {@see HttpFunctionality}
 */
public class HttpFunctionalityImpl implements HttpFunctionality {
    private final ForgeCloseableHttpClient mHttpClient;
    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private boolean mIsShutdowned = false;


    public HttpFunctionalityImpl(ForgeCloseableHttpClient httpClient) {
        if (httpClient != null) {
            mHttpClient = httpClient;
        } else {
            throw new NullPointerException("Argument httpClient is null");
        }
    }


    public String execute(HttpUriRequest httpRequest) throws IOException {
        ResponseHandler<String> responseHandler = new BasicResponseHandlerImpl();

        return mHttpClient.execute(httpRequest, responseHandler);
    }


    @Override
    public <T> T execute(HttpUriRequest httpRequest, ResponseHandler<T> responseHandler) throws
            IOException {

        return mHttpClient.execute(httpRequest, responseHandler);
    }


    public HttpResponse executeForHttpResponse(HttpUriRequest httpRequest) throws
            IOException {

        return mHttpClient.execute(httpRequest);
    }


    public void shutDown() {
        shutDownInBackground();
    }


    // On Honeycomb+ NetworkOnMainThreadException exception is thrown if you try to shutdown on UI thread
    synchronized private void shutDownInBackground() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!mIsShutdowned) {
                    mIsShutdowned = true;
                    try {
                        mHttpClient.close();
                    } catch (IOException e) {
                        mLogger.error("Cannot close the http client", e);
                    }
                }
            }
        });

        t.start();
    }
}
