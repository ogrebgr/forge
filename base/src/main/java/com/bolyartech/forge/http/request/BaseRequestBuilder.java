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
package com.bolyartech.forge.http.request;

import forge.apache.http.Header;
import forge.apache.http.client.methods.HttpGet;
import forge.apache.http.client.methods.HttpRequestBase;
import forge.apache.http.client.methods.HttpUriRequest;
import forge.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;


abstract public class BaseRequestBuilder<T extends HttpRequestBase> extends RequestBuilderImpl {
    abstract protected T createRequest(URI uri);

    public BaseRequestBuilder(String url) {
        super(url);
    }


    @Override
    public HttpUriRequest build() {
        T ret;

        String protocol = getProtocol();
        if (protocol != null && !protocol.equals("") && getDomain() != null
                && !getDomain().equals("") && getPath() != null && !getPath().equals("")) {
            URI uri;
            try {
                URIBuilder ub = new URIBuilder();
                ub.setScheme(protocol).setHost(getDomain()).setPort(getPort()).setPath(getPath());
                if (getGetParams().size() > 0) {
                    ub.setParameters(getGetParams());
                }

                uri = ub.build();
            } catch (URISyntaxException e) {
                throw new IllegalStateException("Error creating URI.", e);
            }

            ret = createRequest(uri);

            if (!isHeaderPresent("Accept-Charset")) {
                ret.addHeader("Accept-Charset", getCharset() + ",*");
            }

            if (getHeaders().size() > 0) {
                for(Header h : getHeaders()) {
                    ret.addHeader(h);
                }
            }

            return ret;
        } else {
            throw new IllegalStateException(String.format(
                    "Some of required fields (protocol (%s), domain (%s), path(%s)) are empty.",
                    getProtocol(),
                    getDomain(),
                    getPath()));
        }
    }
}
