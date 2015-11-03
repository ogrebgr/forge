package com.bolyartech.forge.http.request;

import java.net.URI;
import java.nio.charset.Charset;

import forge.apache.http.client.methods.HttpPut;


/**
 * Created by ogre on 2015-11-01
 */
public class PutRequestBuilder extends EntityEnclosingRequestBuilderImpl<HttpPut> {
    public PutRequestBuilder(String url) {
        super(url);
    }


    public PutRequestBuilder(String url, Charset charset) {
        super(url, charset);
    }


    @Override
    protected HttpPut createRequest(URI uri) {
        return new HttpPut(uri);
    }
}
