package com.bolyartech.forge.http.request;

import java.net.URI;

import forge.apache.http.client.methods.HttpGet;


/**
 * Created by ogre on 2015-12-09 16:47
 */
public class GetRequestBuilder extends BaseRequestBuilder<HttpGet> {
    public GetRequestBuilder(String url) {
        super(url);
    }


    @Override
    protected HttpGet createRequest(URI uri) {
        return new HttpGet(uri);
    }
}
