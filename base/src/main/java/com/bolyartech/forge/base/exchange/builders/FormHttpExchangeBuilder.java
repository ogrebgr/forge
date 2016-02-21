package com.bolyartech.forge.base.exchange.builders;

import com.bolyartech.forge.base.exchange.ResultProducer;
import com.bolyartech.forge.base.exchange.HttpExchange;
import com.bolyartech.forge.base.http.HttpFunctionality;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class FormHttpExchangeBuilder<T> extends GetHttpExchangeBuilder<T> {
    private Map<String, String> mPostParams = new HashMap<>();


    public FormHttpExchangeBuilder(HttpFunctionality httpFunctionality, ResultProducer<T> resultProducer, String url) {
        super(httpFunctionality, resultProducer, url);
    }


    public void addPostParameter(String name, String value) {
        if (!mPostParams.containsKey(name)) {
            mPostParams.put(name, value);
        } else {
            throw new IllegalStateException(MessageFormat.format("POST parameter '{0}' already added.", name));
        }
    }


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

        return new HttpExchange<T>(getHttpFunctionality(),
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
