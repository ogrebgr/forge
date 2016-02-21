package com.bolyartech.forge.base.test_utils;

import com.bolyartech.forge.base.exchange.ResultProducer;
import okhttp3.Response;

import java.io.IOException;

public class StringResultProducer implements ResultProducer<String> {
    @Override
    public String produce(Response resp) throws ResultProducerException {
        try {
            return resp.body().string();
        } catch (IOException e) {
            return null;
        }
    }
}
