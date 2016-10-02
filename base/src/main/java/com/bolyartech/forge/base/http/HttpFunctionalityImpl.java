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

package com.bolyartech.forge.base.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;


/**
 * Implementation of {@see HttpFunctionality}
 */
public class HttpFunctionalityImpl implements HttpFunctionality {
    private final OkHttpClient mOkHttpClient;


    /**
     * Creates new HttpFunctionalityImpl
     * @param okHttpClient HTTP client
     */
    @SuppressWarnings("unused")
    public HttpFunctionalityImpl(OkHttpClient okHttpClient) {
        mOkHttpClient = okHttpClient;
    }


    @Override
    public Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
    }
}
