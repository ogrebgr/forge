package com.bolyartech.forge.base.exchange;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;


public class SimpleExchangeResultProducer implements ResultProducer<SimpleExchangeResult> {
    @Override
    public SimpleExchangeResult produce(Response resp) throws ResultProducerException {
        try {
            if (resp.code() != 204) {
                ResponseBody b = resp.body();
                if (b != null) {
                    String body = b.string();
                    b.close();
                    return new SimpleExchangeResult(resp.code(), body);
                } else {
                    throw new ResultProducerException("Body is empty");
                }
            } else {
                return new SimpleExchangeResult(resp.code(), "");
            }
        } catch (IOException e) {
            throw new ResultProducerException("Error getting response body.", e);
        }
    }
}
