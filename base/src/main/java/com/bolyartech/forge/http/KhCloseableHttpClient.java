package com.bolyartech.forge.http;

import forge.apache.http.client.HttpClient;

import java.io.Closeable;


/**
 * Created by ogre on 2015-10-20
 */
public interface KhCloseableHttpClient extends HttpClient, Closeable {
}
