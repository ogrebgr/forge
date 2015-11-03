package com.bolyartech.forge.http;

import java.io.IOException;

import forge.apache.http.HttpHost;
import forge.apache.http.HttpRequest;
import forge.apache.http.client.ClientProtocolException;
import forge.apache.http.client.ResponseHandler;
import forge.apache.http.client.methods.CloseableHttpResponse;
import forge.apache.http.client.methods.HttpUriRequest;
import forge.apache.http.conn.ClientConnectionManager;
import forge.apache.http.impl.client.CloseableHttpClient;
import forge.apache.http.impl.client.HttpClients;
import forge.apache.http.params.HttpParams;
import forge.apache.http.protocol.HttpContext;


/**
 * Created by ogre on 2015-11-01 16:15
 */
public class SimpleHttpClient implements ForgeCloseableHttpClient {
    private final CloseableHttpClient mHttpClient;


    public SimpleHttpClient() {
        mHttpClient = HttpClients.createDefault();
    }


    @Override
    public CloseableHttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
        return mHttpClient.execute(target, request, context);
    }


    @Override
    public CloseableHttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
        return mHttpClient.execute(request, context);
    }


    @Override
    public CloseableHttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
        return mHttpClient.execute(request);
    }


    @Override
    public CloseableHttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
        return mHttpClient.execute(target, request);
    }


    @Override
    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return mHttpClient.execute(request, responseHandler);
    }


    @Override
    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
        return mHttpClient.execute(request, responseHandler, context);
    }


    @Override
    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return mHttpClient.execute(target, request, responseHandler);
    }


    @Override
    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
        return mHttpClient.execute(target, request, responseHandler, context);
    }


    @Override
    @Deprecated
    public HttpParams getParams() {
        return mHttpClient.getParams();
    }


    @Override
    @Deprecated
    public ClientConnectionManager getConnectionManager() {
        return mHttpClient.getConnectionManager();
    }


    @Override
    public void close() throws IOException {
        mHttpClient.close();
    }
}
