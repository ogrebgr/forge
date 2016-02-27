package com.bolyartech.forge.base.exchange.builders;

import com.bolyartech.forge.base.exchange.ResultProducer;
import com.bolyartech.forge.base.exchange.HttpExchange;
import com.bolyartech.forge.base.http.HttpFunctionality;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.util.Map;


public class GetHttpExchangeBuilder<T> extends HttpExchangeBuilder<T> {
    public GetHttpExchangeBuilder(HttpFunctionality httpFunctionality, ResultProducer<T> resultProducer, String url) {
        super(httpFunctionality, resultProducer, url);
    }


    @Override
    public HttpExchange<T> build() {
        HttpUrl url = createFullUrl();


        Request.Builder b = new Request.Builder();
        b.get();
        b.url(url);

        return new HttpExchange<>(getHttpFunctionality(),
                b.build(),
                getResultProducer());
    }


    protected HttpUrl createFullUrl() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(getUrl()).newBuilder();
        @SuppressWarnings("unchecked") Map<String, String> map = getGetParameters();
        for (String key : map.keySet()) {
            urlBuilder.addQueryParameter(key, map.get(key));
        }

        return urlBuilder.build();
    }
}
