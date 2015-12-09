package com.bolyartech.forge.http.request;

import java.net.URI;

import forge.apache.http.client.methods.HttpDelete;


/**
 * Created by ogre on 2015-12-09 16:55
 */
public class DeleteRequestBuilder extends BaseRequestBuilder<HttpDelete> {
    public DeleteRequestBuilder(String url) {
        super(url);
    }


    @Override
    protected HttpDelete createRequest(URI uri) {
        return new HttpDelete(uri);
    }
}
