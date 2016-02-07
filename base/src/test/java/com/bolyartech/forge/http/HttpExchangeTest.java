package com.bolyartech.forge.http;


import com.bolyartech.forge.exchange.ForgeExchangeResult;
import com.bolyartech.forge.exchange.ResultProducer;
import com.bolyartech.forge.http.HttpExchange;
import com.bolyartech.forge.http.functionality.HttpFunctionality;
import forge.apache.http.client.methods.HttpUriRequest;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


/**
 * Created by ogre on 2015-10-14
 */
public class HttpExchangeTest {
    @Test(expected = NullPointerException.class)
    public void test_constructor1() {
        HttpExchange<ForgeExchangeResult> impl = new HttpExchange<>(null, null, null, null);
    }


    @Test(expected = NullPointerException.class)
    public void test_constructor2() {
        HttpUriRequest req = mock(HttpUriRequest.class);
        HttpExchange<ForgeExchangeResult> impl = new HttpExchange<>(null, req, null, null);
    }


    @Test(expected = NullPointerException.class)
    public void test_constructor3() {
        HttpUriRequest req = mock(HttpUriRequest.class);
        ResultProducer rp = mock(ResultProducer.class);
        HttpExchange<ForgeExchangeResult> impl = new HttpExchange<>(null, req, rp, null);
    }


    /**
     * Tests if class will NOT throw NullPointerException when all parameters are present
     */
    @Test
    public void test_constructor4() {
        HttpUriRequest req = mock(HttpUriRequest.class);
        ResultProducer<ForgeExchangeResult> rp = mock(ResultProducer.class);

        HttpExchange<ForgeExchangeResult> impl = new HttpExchange<>(null, req, rp, ForgeExchangeResult.class);

        assertTrue(true);
    }


//    @Test
//    public void test_createResult() throws ResultProducer.ResultProducerException {
//
//        HttpUriRequest req = mock(HttpUriRequest.class);
//        ResultProducer rp = mock(ResultProducer.class);
//        ForgeExchangeResult rez = new ForgeExchangeResult(1, "");
//        when(rp.produce(anyString(), any(Class.class))).thenReturn(rez);
//
//        HttpExchange<ForgeExchangeResult> impl = new HttpExchange<>(req, rp, ForgeExchangeResult.class);
//
//        ForgeExchangeResult rez2 = impl.createResult("");
//
//        assertTrue(rez == rez2);
//    }
//
//
//    @Test
//    public void test_execute_cancelled() throws IOException, ResultProducer.ResultProducerException {
//        HttpUriRequest req = mock(HttpUriRequest.class);
//        ResultProducer rp = mock(ResultProducer.class);
//        ForgeExchangeResult rez = new ForgeExchangeResult(1, "");
//        when(rp.produce(anyString(), any(Class.class))).thenReturn(rez);
//
//        HttpExchange<ForgeExchangeResult> impl = new HttpExchange<>(req, rp, ForgeExchangeResult.class);
//        impl.cancel();
//
//        HttpFunctionality http = mock(HttpFunctionality.class);
//        ForgeExchangeResult rez2 = impl.execute();
//
//        assertTrue(rez2 == null);
//    }
//
//
//    @Test(expected = IllegalStateException.class)
//    public void test_execute_executed() throws IOException, ResultProducer.ResultProducerException {
//        HttpUriRequest req = mock(HttpUriRequest.class);
//        when(req.getURI()).thenReturn(URI.create("http://somehost.com/"));
//        ResultProducer rp = mock(ResultProducer.class);
//        ForgeExchangeResult rez = new ForgeExchangeResult(1, "");
//        when(rp.produce(anyString(), any(Class.class))).thenReturn(rez);
//
//        HttpExchange<ForgeExchangeResult> impl = new HttpExchange<>(req, rp, ForgeExchangeResult.class);
//
//        HttpFunctionality http = mock(HttpFunctionality.class);
//        when(http.execute(req)).thenReturn("some string");
//        impl.execute();
//        impl.execute();
//    }
//
//
//    @Test
//    public void test_execute() throws IOException, ResultProducer.ResultProducerException {
//        HttpUriRequest req = mock(HttpUriRequest.class);
//        when(req.getURI()).thenReturn(URI.create("http://somehost.com/"));
//        ResultProducer rp = mock(ResultProducer.class);
//        ForgeExchangeResult rez = new ForgeExchangeResult(1, "");
//        when(rp.produce(anyString(), any(Class.class))).thenReturn(rez);
//
//        HttpExchange<ForgeExchangeResult> impl = new HttpExchange<>(req, rp, ForgeExchangeResult.class);
//
//        HttpFunctionality http = mock(HttpFunctionality.class);
//        when(http.execute(req)).thenReturn("some string");
//        ForgeExchangeResult rez2 = impl.execute();
//
//        assertTrue(rez == rez2);
//    }
}