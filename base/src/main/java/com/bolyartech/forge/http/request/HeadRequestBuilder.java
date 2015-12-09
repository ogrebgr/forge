package com.bolyartech.forge.http.request;

import java.net.URI;

import forge.apache.http.client.methods.HttpHead;


/**
 * Created by ogre on 2015-12-09 16:56
 */
public class HeadRequestBuilder extends BaseRequestBuilder<HttpHead> {
    public HeadRequestBuilder(String url) {
        super(url);
    }


    @Override
    protected HttpHead createRequest(URI uri) {
        return new HttpHead(uri);
    }
}
