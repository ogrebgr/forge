package com.bolyartech.forge.http.ssl;

import com.bolyartech.forge.http.ForgeCloseableHttpClient;
import forge.apache.http.HttpHost;
import forge.apache.http.HttpRequest;
import forge.apache.http.client.ResponseHandler;
import forge.apache.http.client.methods.CloseableHttpResponse;
import forge.apache.http.client.methods.HttpUriRequest;
import forge.apache.http.config.Registry;
import forge.apache.http.config.RegistryBuilder;
import forge.apache.http.conn.ClientConnectionManager;
import forge.apache.http.conn.socket.ConnectionSocketFactory;
import forge.apache.http.conn.ssl.SSLConnectionSocketFactory;
import forge.apache.http.conn.ssl.SSLContextBuilder;
import forge.apache.http.impl.client.CloseableHttpClient;
import forge.apache.http.impl.client.HttpClients;
import forge.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import forge.apache.http.params.HttpParams;
import forge.apache.http.protocol.HttpContext;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;


/**
 * Created by ogre on 2015-10-10
 */
public class DefaultSslHttpClient implements ForgeCloseableHttpClient {
    private final CloseableHttpClient mHttpClient;

    @SuppressWarnings("FieldCanBeLocal")
    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(this.getClass().getSimpleName());


    //TODO make it to use inject parameter
    public DefaultSslHttpClient(InputStream keyStore, String keyStorePassword) {
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();

        if (keyStore == null || keyStorePassword == null) {
            throw new NullPointerException();
        }

        KeyStore ks;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(keyStore, keyStorePassword.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            mLogger.error("Cannot create the keystore", e);
            throw new IllegalStateException("Cannot create the keystore");
        } finally {
            try {
                keyStore.close();
            } catch (IOException e) {
                mLogger.error("Cannot create the keystore", e);
            }
        }


        try {
            sslContextBuilder.loadTrustMaterial(ks);
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            mLogger.error("Cannot load trust material", e);
        }

        try {
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContextBuilder.build());
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                    .<ConnectionSocketFactory>create().register("https", sslsf)
                    .build();


            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
                    socketFactoryRegistry);
            mHttpClient = HttpClients.custom().setConnectionManager(cm).build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            mLogger.error("Cannot load trust material", e);
            throw new IllegalStateException("Unable to create http client");
        }
    }


    @Override
    public CloseableHttpResponse execute(HttpUriRequest httpUriRequest) throws IOException {
        return mHttpClient.execute(httpUriRequest);
    }


    @Override
    public CloseableHttpResponse execute(HttpUriRequest httpUriRequest,
                                         HttpContext httpContext
    ) throws IOException {
        return mHttpClient.execute(httpUriRequest, httpContext);
    }


    @Override
    public CloseableHttpResponse execute(HttpHost httpHost,
                                         HttpRequest httpRequest
    ) throws IOException {
        return mHttpClient.execute(httpHost, httpRequest);
    }


    @Override
    public CloseableHttpResponse execute(HttpHost httpHost,
                                         HttpRequest httpRequest,
                                         HttpContext httpContext
    ) throws IOException {
        return mHttpClient.execute(httpHost, httpRequest, httpContext);
    }


    @Override
    public <T> T execute(HttpUriRequest httpUriRequest,
                         ResponseHandler<? extends T> responseHandler
    ) throws IOException {
        return mHttpClient.execute(httpUriRequest, responseHandler);
    }


    @Override
    public <T> T execute(HttpUriRequest httpUriRequest,
                         ResponseHandler<? extends T> responseHandler,
                         HttpContext httpContext
    ) throws IOException {
        return mHttpClient.execute(httpUriRequest, responseHandler, httpContext);
    }


    @Override
    public <T> T execute(HttpHost httpHost,
                         HttpRequest httpRequest,
                         ResponseHandler<? extends T> responseHandler
    ) throws IOException {
        return mHttpClient.execute(httpHost, httpRequest, responseHandler);
    }


    @Override
    public <T> T execute(HttpHost httpHost,
                         HttpRequest httpRequest,
                         ResponseHandler<? extends T> responseHandler,
                         HttpContext httpContext
    ) throws IOException {
        return mHttpClient.execute(httpHost, httpRequest, responseHandler, httpContext);
    }


    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public HttpParams getParams() {
        return mHttpClient.getParams();
    }


    @SuppressWarnings("deprecation")
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
