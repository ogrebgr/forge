package com.bolyartech.forge.base.exchange.builders;

import com.bolyartech.forge.base.exchange.HttpExchange;
import com.bolyartech.forge.base.exchange.ResultProducer;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;


/**
 * Base class for POST/PUT exchange builders
 * @param <T>
 */
@SuppressWarnings("WeakerAccess")
public class FormHttpExchangeBuilder<T> extends GetHttpExchangeBuilder<T> {
    private final Map<String, String> mPostParams = new HashMap<>();


    /**
     * Creates new FormHttpExchangeBuilder
     * @param okHttpClient OkHttpClient to be used
     * @param resultProducer Result producer
     * @param url URL of the endpoint
     */
    public FormHttpExchangeBuilder(OkHttpClient okHttpClient, ResultProducer<T> resultProducer, String url) {
        super(okHttpClient, resultProducer, url);
    }


    /**
     * Adds new POST parameter
     * @param name Name of the parameter
     * @param value Value of the parameter
     */
    @SuppressWarnings("unused")
    public void addPostParameter(String name, String value) {
        if (value == null) {
            throw new NullPointerException("value == null");
        }


        if (mPostParams.containsKey(name)) {
            throw new IllegalStateException(MessageFormat.format("POST parameter '{0}' already added.", name));
        }

        mPostParams.put(name, value);
    }


    /**
     * Checks if POST parameter is present
     * @param name Name of the parameter
     * @return true if parameter is already added, false otherwise
     */
    @SuppressWarnings("unused")
    public boolean isPostParameterPresent(String name) {
        return mPostParams.containsKey(name);
    }


    @Override
    public HttpExchange<T> build() {
        HttpUrl url = createFullUrl();

        FormBody fb = createFormBody();

        Request.Builder b = new Request.Builder();
        b.post(fb);
        b.url(url);
        b.cacheControl(CacheControl.FORCE_NETWORK);
        for (String key : mHeaderParams.keySet()) {
            b.addHeader(key, mHeaderParams.get(key));
        }
        return new HttpExchange<>(getHttpClient(),
                b.build(),
                getResultProducer());
    }


    private FormBody createFormBody() {
        FormBody.Builder fb = new FormBody.Builder();

        for (String key : mPostParams.keySet()) {
            fb.add(key, mPostParams.get(key));
        }

        return fb.build();
    }
}
