package com.bolyartech.forge.http.request;

import forge.apache.http.client.methods.HttpGet;
import forge.apache.http.client.methods.HttpUriRequest;
import forge.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;


public class GetRequestBuilderImpl extends RequestBuilderImpl {
    public GetRequestBuilderImpl(String url) {
        super(url);
    }


    @Override
    public HttpUriRequest build() {
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

            HttpGet ret = new HttpGet(uri);
            ret.addHeader("Accept-Charset", getCharset() + ",*");

            return ret;
        } else {
            throw new IllegalStateException(String.format(
                    "Some of required fields (protocol (%s), domain (%s), path(%s)) are empty.",
                    getProtocol(),
                    getDomain(),
                    getPath()));
        }
    }
}
