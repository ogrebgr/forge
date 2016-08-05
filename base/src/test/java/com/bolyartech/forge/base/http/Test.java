package com.bolyartech.forge.base.http;

import com.bolyartech.forge.base.exchange.ResultProducer;
import com.bolyartech.forge.base.exchange.builders.FormHttpExchangeBuilder;
import com.bolyartech.forge.base.task.ExchangeManager;
import com.bolyartech.forge.base.task.ExchangeManagerImpl;
import com.bolyartech.forge.base.task.TaskExecutor;
import com.bolyartech.forge.base.task.TaskExecutorImpl;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.io.IOException;

public class Test {
    @org.junit.Test
    public void getTest() {
        OkHttpClient client = new OkHttpClient();
        HttpFunctionality http = new HttpFunctionalityImpl(client);
        ResultProducer<String> rp = new ResultProducer<String>() {
            @Override
            public String produce(Response resp) throws ResultProducerException {
                try {
                    return resp.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        FormHttpExchangeBuilder<String> b = new FormHttpExchangeBuilder<>(http, rp, "http://ggogre.bolyartech.com/okhttp.php");
        b.addGetParameter("presni", "chudesni");
        b.addPostParameter("hrupkavi", "lajna");

        TaskExecutor te = new TaskExecutorImpl();
        te.start();
        @SuppressWarnings("unchecked") ExchangeManager<String> em = new ExchangeManagerImpl<String>(te);
        em.addListener(new ExchangeManager.Listener<String>() {
            @Override
            public void onExchangeOutcome(long exchangeId, boolean isSuccess, String result) {
            }
        });
        em.executeExchange(b.build());

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        te.shutdown();
    }
}
