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

import com.bolyartech.forge.misc.ForUnitTestsOnly;
import com.google.common.base.Charsets;
import forge.apache.http.NameValuePair;
import forge.apache.http.client.entity.UrlEncodedFormEntity;
import forge.apache.http.client.methods.HttpPost;
import forge.apache.http.client.utils.URIBuilder;
import forge.apache.http.entity.ContentType;
import forge.apache.http.entity.mime.MultipartEntityBuilder;
import forge.apache.http.entity.mime.content.FileBody;
import forge.apache.http.entity.mime.content.StringBody;
import forge.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PostRequestBuilderImpl extends GetRequestBuilderImpl implements PostRequestBuilder {
    private static final Charset DEFAULT_CHARSET = Charsets.UTF_8;

    private final ArrayList<NameValuePair> mPostParams = new ArrayList<>();
    private final Map<String, File> mFilesToUpload = new HashMap<>();
    private final Charset mCharset;


    public PostRequestBuilderImpl(String url) {
        this(url, DEFAULT_CHARSET);
    }


    @SuppressWarnings("SameParameterValue")
    public PostRequestBuilderImpl(String url, Charset charset) {
        super(url);

        if (charset != null) {
            mCharset = charset;
        } else {
            throw new NullPointerException("Parameter 'charset' cannot be null");
        }

    }


    public HttpPost build() {
        HttpPost ret;

        String protocol = getProtocol();
        if (protocol != null && !protocol.equals("") && getDomain() != null
                && !getDomain().equals("") && getPath() != null && !getPath().equals("")) {
            URI uri;
            try {
                URIBuilder ub = new URIBuilder();
                ub.setScheme(protocol).setHost(getDomain()).setPort(getPort()).setPath(getPath());
                ub.setParameters(getGetParams());
                uri = ub.build();
            } catch (URISyntaxException e) {
                throw new IllegalStateException("Error creating URI.", e);
            }

            ret = new HttpPost(uri);
            ret.addHeader("Accept-Charset", mCharset + ",*");

            if (mFilesToUpload.size() == 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(mPostParams, mCharset);
                ret.setEntity(entity);
            } else {
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

                for (String key : mFilesToUpload.keySet()) {
                    entityBuilder.addPart(key, new FileBody(mFilesToUpload.get(key)));
                }

                ContentType ct = ContentType.TEXT_PLAIN;
                for (NameValuePair p : mPostParams) {
                    entityBuilder.addPart(p.getName(), new StringBody(p.getValue(), ct));
                }

                ret.setEntity(entityBuilder.build());
            }

        } else {
            throw new IllegalStateException("Some of required fields (protocol, domain, path) are empty.");
        }

        return ret;
    }


    public void addPostParameter(String paramName, String value) {
        if (isPostParameterMissing(paramName) && !mFilesToUpload.containsKey(paramName)) {
            NameValuePair tmp = new BasicNameValuePair(paramName, value);
            mPostParams.add(tmp);
        } else {
            throw new IllegalArgumentException("There is already POST parameter named " + paramName);
        }
    }


    @Override
    public boolean isPostParameterPresent(String paramName) {
        return !isPostParameterMissing(paramName);
    }


    /**
     * Checks if POST parameter is NOT present
     *
     * @param paramName Name of the parameter
     * @return true if NOT present, false otherwise
     */
    public boolean isPostParameterMissing(String paramName) {
        boolean ret = false;

        for (NameValuePair pair : mPostParams) {
            if (pair.getName().equals(paramName)) {
                ret = true;
                break;
            }
        }

        return !ret;
    }


    public void addFileToUpload(String paramName, File file) {
        if (!mFilesToUpload.containsKey(paramName) && isPostParameterMissing(paramName)) {
            if (isPostParameterMissing(paramName)) {
                if (file.exists()) {
                    mFilesToUpload.put(paramName, file);
                } else {
                    throw new IllegalArgumentException("File does not exist "
                            + file.getAbsolutePath());
                }
            } else {
                throw new IllegalArgumentException("There is already POST parameter named "
                        + paramName);
            }
        } else {
            throw new IllegalArgumentException("There is already added file for upload for param name '"
                    + paramName + "'");
        }
    }


    @ForUnitTestsOnly
    ArrayList<NameValuePair> getPostParams() {
        return mPostParams;
    }


    @ForUnitTestsOnly
    Map<String, File> getFilesToUpload() {
        return mFilesToUpload;
    }
}
