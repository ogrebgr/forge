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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import forge.apache.http.Header;
import forge.apache.http.NameValuePair;
import forge.apache.http.client.methods.HttpUriRequest;
import forge.apache.http.client.utils.URLEncodedUtils;
import forge.apache.http.message.BasicHeader;
import forge.apache.http.message.BasicNameValuePair;


/**
 * Abstract base for request builders
 */
abstract public class RequestBuilderImpl implements RequestBuilder {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private final String mCharset;
    private final List<NameValuePair> mGetParams = new ArrayList<>();
    private final List<Header> mHeaders = new ArrayList<>();
    private String mProtocol;
    private String mDomain;
    private String mPath;
    private int mPort;

    private ProgressListener mProgressListener;


    /**
     * Creates the builder with default charset = utf-8. If GET parameters are present in the URL they are parsed and added.
     *
     * @param url URL of the request
     */
    public RequestBuilderImpl(String url) {
        this(url, DEFAULT_CHARSET);
    }


    /**
     * Creates the builder. If GET parameters are present in the URL they are parsed and added.
     *
     * @param url     URL of the request
     * @param charset Charset to be used
     */
    @SuppressWarnings("SameParameterValue")
    public RequestBuilderImpl(String url, String charset) {
        super();

        if (charset != null) {
            mCharset = charset;
        } else {
            throw new NullPointerException("Parameter 'charset' cannot be null");
        }

        if (url != null) {
            URI tmpUri;
            try {
                tmpUri = new URI(url);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Invalid URL: " + url, e);
            }

            protocol(tmpUri.getScheme());
            mDomain = tmpUri.getHost();
            mPath = tmpUri.getPath();
            mPort = tmpUri.getPort();

            for (NameValuePair pair : URLEncodedUtils.parse(tmpUri, mCharset)) {
                mGetParams.add(pair);
            }
        } else {
            throw new NullPointerException("Parameter 'url' is null.");
        }
    }


    /**
     * Builds the request
     *
     * @return Built request
     */
    abstract public HttpUriRequest build();


    /**
     * Adds GET parameter
     *
     * @param paramName Name of the parameter
     * @param value     Value of the parameter
     */
    public void parameter(String paramName, String value) {
        if (!isParameterPresent(paramName)) {
            mGetParams.add(new BasicNameValuePair(paramName, value));
        } else {
            throw new IllegalArgumentException("There is already parameter named " + paramName);
        }
    }


    @Override
    public boolean isParameterPresent(String paramName) {
        boolean ret = false;

        for (NameValuePair pair : mGetParams) {
            if (pair.getName().equals(paramName)) {
                ret = true;
                break;
            }
        }

        return ret;
    }


    @Override
    public void header(String name, String value) {
        if (!isHeaderPresent(name)) {
            mHeaders.add(new BasicHeader(name, value));
        } else {
            throw new IllegalArgumentException("There is already parameter named " + name);
        }
    }


    @Override
    public boolean isHeaderPresent(String name) {
        boolean ret = false;

        for (Header pair : mHeaders) {
            if (pair.getName().equals(name)) {
                ret = true;
                break;
            }
        }

        return ret;
    }


    /**
     * Returns the protocol which is extracted from the request URL
     *
     * @return Protocol which is extracted from the request URL
     */
    protected String getProtocol() {
        return mProtocol;
    }


    private void protocol(String protocol) {
        String protoLower = protocol.toLowerCase();
        if (protoLower.equals("http") || protoLower.equals("https")) {
            this.mProtocol = protocol;
        } else {
            throw new IllegalArgumentException("Protocol must be 'http' ot 'https'");
        }
    }


    /**
     * @return domain which is extracted from the request URL
     */
    protected String getDomain() {
        return mDomain;
    }


    /**
     * @return path which is extracted from the request URL
     */
    protected String getPath() {
        return mPath;
    }


    /**
     * @return port which is extracted from the request URL
     */
    protected int getPort() {
        return mPort;
    }


    /**
     * @return Get params (defensive copy)
     */
    protected List<NameValuePair> getGetParams() {
        return new ArrayList<>(mGetParams);
    }


    /**
     * @return Initially set charset
     */
    protected String getCharset() {
        return mCharset;
    }


    @Override
    public void progressListener(ProgressListener progressListener) {
        mProgressListener = progressListener;
    }


    protected ProgressListener getProgressListener() {
        return mProgressListener;
    }


    protected List<Header> getHeaders() {
        return mHeaders;
    }
}
