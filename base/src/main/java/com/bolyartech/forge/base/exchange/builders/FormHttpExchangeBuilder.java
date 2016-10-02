package com.bolyartech.forge.base.exchange.builders;

import com.bolyartech.forge.base.exchange.ResultProducer;
import com.bolyartech.forge.base.exchange.HttpExchange;
import com.bolyartech.forge.base.http.HttpFunctionality;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * Base class for POST/PUT exchange builders
 * @param <T>
 */
@SuppressWarnings("WeakerAccess")
public class FormHttpExchangeBuilder<T> extends GetHttpExchangeBuilder<T> {
    private final Map<String, String> mPostParams = new HashMap<>();


    /**
     * Creates new FormHttpExchangeBuilder
     * @param httpFunctionality HTTP functionality
     * @param resultProducer Result producer
     * @param url URL of the endpoint
     */
    public FormHttpExchangeBuilder(HttpFunctionality httpFunctionality, ResultProducer<T> resultProducer, String url) {
        super(httpFunctionality, resultProducer, url);
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

        return new HttpExchange<>(getHttpFunctionality(),
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
