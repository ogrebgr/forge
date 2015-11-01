package com.bolyartech.forge.http.request;

import java.net.URI;
import java.nio.charset.Charset;

import forge.apache.http.client.methods.HttpPost;


/**
 * Created by ogre on 2015-11-01
 */
public class PostRequestBuilderImpl extends EntityEnclosingRequestBuilderImpl<HttpPost> {
    public PostRequestBuilderImpl(String url) {
        super(url);
    }


    public PostRequestBuilderImpl(String url, Charset charset) {
        super(url, charset);
    }


    @Override
    protected HttpPost createRequest(URI uri) {
        return new HttpPost(uri);
    }
}
