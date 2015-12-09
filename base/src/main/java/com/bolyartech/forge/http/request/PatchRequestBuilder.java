package com.bolyartech.forge.http.request;

import java.net.URI;

import forge.apache.http.client.methods.HttpPatch;


/**
 * Created by ogre on 2015-12-09 16:57
 */
public class PatchRequestBuilder extends EntityEnclosingRequestBuilderImpl<HttpPatch> {
    public PatchRequestBuilder(String url) {
        super(url);
    }


    @Override
    protected HttpPatch createRequest(URI uri) {
        return new HttpPatch(uri);
    }
}
