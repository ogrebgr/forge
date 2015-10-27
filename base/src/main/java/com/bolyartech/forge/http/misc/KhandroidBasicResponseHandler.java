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

package com.bolyartech.forge.http.misc;

import forge.apache.http.HttpEntity;
import forge.apache.http.StatusLine;
import forge.apache.http.client.HttpResponseException;
import forge.apache.http.client.methods.CloseableHttpResponse;
import forge.apache.http.impl.client.BasicResponseHandler;
import forge.apache.http.util.EntityUtils;

import java.io.IOException;


/**
 * Basic response handler that "extracts" response body from HttpResponse and returns it as a String
 */
public class KhandroidBasicResponseHandler extends BasicResponseHandler {
    /**
     * Handles HttpResponse and if it is successful extracts its body and returns it as a String
     *
     * @param response HttpResponse to be handled
     * @return String that contains the body of the response
     * @throws IOException if network error occurs
     */
    public String handleResponse(final CloseableHttpResponse response)
            throws
            IOException {


        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() >= 300) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                EntityUtils.consume(entity);
            }

            throw new HttpResponseException(statusLine.getStatusCode(),
                    statusLine.getReasonPhrase());
        }

        HttpEntity entity = response.getEntity();
        response.close();
        return entity == null ? null : EntityUtils.toString(entity);
    }
}