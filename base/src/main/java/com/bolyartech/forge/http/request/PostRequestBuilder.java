package com.bolyartech.forge.http.request;

import java.net.URI;
import java.nio.charset.Charset;

import forge.apache.http.client.methods.HttpPost;


/**
 * Created by ogre on 2015-11-01
 */
public class PostRequestBuilder extends EntityEnclosingRequestBuilderImpl<HttpPost> {
    public PostRequestBuilder(String url) {
        super(url);
    }


    public PostRequestBuilder(String url, Charset charset) {
        super(url, charset);
    }


    @Override
    protected HttpPost createRequest(URI uri) {
        return new HttpPost(uri);
    }
}
