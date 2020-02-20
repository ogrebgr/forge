package com.bolyartech.forge.base.exchange;

public class SimpleExchangeResult {
    private final int httpCode;
    private final String payload;


    public SimpleExchangeResult(int httpCode, String payload) {
        this.httpCode = httpCode;
        this.payload = payload;
    }


    public int getHttpCode() {
        return httpCode;
    }


    public String getPayload() {
        return payload;
    }
}
