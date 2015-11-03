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

package com.bolyartech.forge.exchange;

import com.bolyartech.forge.http.request.EntityEnclosingRequestBuilderImpl;
import com.bolyartech.forge.http.request.GetRequestBuilder;
import com.bolyartech.forge.http.request.PostRequestBuilder;
import com.bolyartech.forge.http.request.PutRequestBuilder;
import com.bolyartech.forge.misc.StringUtils;
import forge.apache.http.NameValuePair;
import forge.apache.http.client.methods.HttpUriRequest;
import forge.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Builder for exchange exchange
 *
 * @param <T> Type of the exchange result
 */
@SuppressWarnings("WeakerAccess")
public class RestExchangeBuilder<T> {
    /**
     * GET parameters
     */
    private final List<NameValuePair> mGetParams = new ArrayList<>();
    /**
     * POST parameters
     */
    private final List<NameValuePair> mPostParams = new ArrayList<>();
    /**
     * Files to upload
     */
    private final Map<String, File> mFilesToUpload = new HashMap<>();
    /**
     * Base URL like <code>http://somehost.com'</code>
     */
    private String mBaseUrl;
    /**
     * Concrete endpoint like <code>somepage.php'</code>
     */
    private String mEndpoint;
    /**
     * Class of the result of the exchange
     */
    private Class<T> mResultClass;
    /**
     * JSON functionality that will be used to convert JSON string to result object of type <code>T</code>
     */
    private ResultProducer<T> mResultProducer;
    /**
     * Tag object
     */
    private Object mTag;
    /**
     * Request type
     */
    private RequestType mRequestType = RequestType.POST;


    /**
     * Creates new RestExchangeBuilder
     */
    public RestExchangeBuilder() {
        super();
    }


    /**
     * Creates new RestExchangeBuilder
     *
     * @param baseUrl     Base URL like <code>http://somehost.com'</code>
     * @param endpoint    Concrete endpoint like <code>somepage.php'</code>
     * @param resultClass Class of the result of the exchange
     * @param resultProducer        JSON functionality that will be used to convert JSON string to result object of type <code>T</code>
     */
    public RestExchangeBuilder(String baseUrl,
                               String endpoint,
                               Class<T> resultClass,
                               ResultProducer<T> resultProducer
    ) {
        super();

        if (baseUrl == null) {
            throw new NullPointerException("Parameter 'baseUrl' is null");
        }

        if (endpoint == null) {
            throw new NullPointerException("Parameter 'endpoint' is null");
        }

        if (resultClass == null) {
            throw new NullPointerException("Parameter 'resultClass' is null");
        }

        if (resultProducer == null) {
            throw new NullPointerException("Parameter 'resultProducer' is null");
        }

        mBaseUrl = baseUrl;
        mResultClass = resultClass;
        mEndpoint = endpoint;
        mResultProducer = resultProducer;
    }


    /**
     * Sets base URL
     *
     * @param baseUrl base URL
     * @return the builder itself
     */
    public RestExchangeBuilder<T> baseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
        return this;
    }


    /**
     * Sets the endpoint/page
     *
     * @param endpoint endpoint/web page (relative to base URL)
     * @return the builder itself
     */
    public RestExchangeBuilder<T> endpoint(String endpoint) {
        mEndpoint = endpoint;
        return this;
    }


    /**
     * Sets the result class for type <code>T</code>
     *
     * @param resultClass Result class for type <code>T</code>
     * @return the builder itself
     */
    public RestExchangeBuilder<T> resultClass(Class<T> resultClass) {
        mResultClass = resultClass;
        return this;
    }


    /**
     * Sets JSON functionality
     *
     * @param resultProducer JSON functionality
     * @return the builder itself
     */
    public RestExchangeBuilder<T> resultProducer(ResultProducer<T> resultProducer) {
        mResultProducer = resultProducer;
        return this;
    }


    /**
     * Sets the request type
     *
     * @param type request type
     * @return the builder itself
     */
    public RestExchangeBuilder<T> requestType(RequestType type) {
        mRequestType = type;
        return this;
    }


    /**
     * Builds the Exchange
     *
     * @return The build exchange
     */
    public Exchange<T> build() {
        checkRequired();

        return new ExchangeImpl<>(mRequestType.createRequest(this), mResultProducer, mResultClass, mTag);
    }


    /**
     * Adds POST parameter
     *
     * @param key   Parameter name
     * @param value Parameter value
     */
    public void addPostParameter(String key, String value) {
        if (isPostParameterMissing(key) || mFilesToUpload.containsKey(key)) {
            NameValuePair tmp = new BasicNameValuePair(key, value);
            mPostParams.add(tmp);
        } else {
            throw new IllegalArgumentException("There is already POST parameter named " + key);
        }
    }


    /**
     * Checks if POST parameter is already added
     *
     * @param key Parameter name
     * @return <code>true</code> if already added, <code>false</code> oterwise
     */
    public boolean isPostParameterMissing(String key) {
        boolean ret = false;

        for (NameValuePair pair : mPostParams) {
            if (pair.getName().equals(key)) {
                ret = true;
                break;
            }
        }

        return !ret;
    }


    /**
     * Adds GET parameter
     *
     * @param key   Parameter name
     * @param value Parameter value
     */
    public void addGetParameter(String key, String value) {
        if (!isParameterPresent(key)) {
            NameValuePair tmp = new BasicNameValuePair(key, value);
            mGetParams.add(tmp);
        } else {
            throw new IllegalArgumentException("There is already GET parameter named " + key);
        }
    }


    /**
     * Checks if GET parameter is already added
     *
     * @param key Parameter name
     * @return <code>true</code> if already added, <code>false</code> oterwise
     */
    public boolean isParameterPresent(String key) {
        boolean ret = false;

        for (NameValuePair pair : mGetParams) {
            if (pair.getName().equals(key)) {
                ret = true;
                break;
            }
        }

        return ret;
    }


    /**
     * Sets tag object
     *
     * @param tag object to be used as tag
     */
    public void tag(Object tag) {
        mTag = tag;
    }


    /**
     * Adds file to upload
     *
     * @param paramName POST parameter name
     * @param file      file to upload
     */
    public void addFileToUpload(String paramName, File file) {
        if (mRequestType == RequestType.POST || mRequestType == RequestType.PUT) {
            if (!mFilesToUpload.containsKey(paramName)) {
                if (isPostParameterMissing(paramName)) {
                    if (file.exists()) {
                        mFilesToUpload.put(paramName, file);
                    } else {
                        throw new IllegalArgumentException("File does not exist " + file.getAbsolutePath());
                    }
                } else {
                    throw new IllegalArgumentException("There is already POST parameter named " + paramName);
                }
            } else {
                throw new IllegalArgumentException("There is already added file for upload for param name '" + paramName + "'");
            }
        } else {
            throw new IllegalStateException(
                    "Need POST request type in order to upload files. Please call requestType(RequestType.POST) before current method.");
        }
    }


    /**
     * Checks if all requires attributes are set
     */
    private void checkRequired() {
        if (StringUtils.isEmpty(mBaseUrl)) {
            throw new IllegalStateException("base URL not set");
        } else {
            if (StringUtils.isEmpty(mEndpoint)) {
                throw new IllegalStateException("endpoint not set");
            } else {
                if (mResultClass == null) {
                    throw new IllegalStateException("result class not set");
                }
            }
        }
    }




    /**
     * Request type
     */
    public enum RequestType {
        GET {
            @Override
            HttpUriRequest createRequest(RestExchangeBuilder builder) {
                return createGetRequest(builder);
            }
        },
        POST {
            @Override
            HttpUriRequest createRequest(RestExchangeBuilder builder) {
                return createPostRequest(builder);
            }
        },
        PUT {
            @Override
            HttpUriRequest createRequest(RestExchangeBuilder builder) {
                return createPutRequest(builder);
            }
        };


        abstract HttpUriRequest createRequest(RestExchangeBuilder builder);


        private void test(NameValuePair p) {
            p.getName();
        }


        private static HttpUriRequest createGetRequest(RestExchangeBuilder builder) {
            if (builder.mPostParams.size() > 0) {
                throw new IllegalStateException("You requested GET request but added some POST parameters.");
            }

            GetRequestBuilder b = new GetRequestBuilder(builder.mBaseUrl + builder.mEndpoint);

            @SuppressWarnings("unchecked") List<NameValuePair> getParams = builder.mGetParams;
            for (NameValuePair p : getParams) {
                b.addParameter(p.getName(), p.getValue());
            }

            return b.build();
        }


        private static HttpUriRequest createPostRequest(RestExchangeBuilder builder) {
            PostRequestBuilder b = new PostRequestBuilder(builder.mBaseUrl + builder.mEndpoint);

            return createEntityEnclosingRequest(b, builder);
        }


        private static HttpUriRequest createPutRequest(RestExchangeBuilder builder) {
            PutRequestBuilder b = new PutRequestBuilder(builder.mBaseUrl + builder.mEndpoint);

            return createEntityEnclosingRequest(b, builder);
        }


        private static HttpUriRequest createEntityEnclosingRequest(EntityEnclosingRequestBuilderImpl b, RestExchangeBuilder builder) {
            @SuppressWarnings("unchecked") List<NameValuePair> postParams = builder.mPostParams;

            for (NameValuePair p : postParams) {
                b.addPostParameter(p.getName(), p.getValue());
            }

            @SuppressWarnings("unchecked") Map<String, File> filesToUpload = builder.mFilesToUpload;
            for (String key : filesToUpload.keySet()) {
                b.addFileToUpload(key, filesToUpload.get(key));
            }

            return b.build();
        }
    }
}
