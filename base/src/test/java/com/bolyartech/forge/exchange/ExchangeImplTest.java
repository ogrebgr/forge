package com.bolyartech.forge.exchange;


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
public class ExchangeImplTest {
    @Test(expected = NullPointerException.class)
    public void test_constructor1() {
        ExchangeImpl impl = new ExchangeImpl(null, null, null);
    }


    @Test(expected = NullPointerException.class)
    public void test_constructor2() {
        HttpUriRequest req = mock(HttpUriRequest.class);
        ExchangeImpl<ForgeExchangeResult> impl = new ExchangeImpl<>(req, null, null);
    }


    @Test(expected = NullPointerException.class)
    public void test_constructor3() {
        HttpUriRequest req = mock(HttpUriRequest.class);
        ResultProducer json = mock(ResultProducer.class);
        ExchangeImpl<ForgeExchangeResult> impl = new ExchangeImpl<>(req, json, null);
    }


    /**
     * Tests if class will NOT throw NullPointerException when all parameters are present
     */
    @Test
    public void test_constructor4() {
        HttpUriRequest req = mock(HttpUriRequest.class);
        ResultProducer json = mock(ResultProducer.class);

        ExchangeImpl<ForgeExchangeResult> impl = new ExchangeImpl<>(req, json, ForgeExchangeResult.class);

        assertTrue(true);
    }


    @Test
    public void test_createResult() {
        HttpUriRequest req = mock(HttpUriRequest.class);
        ResultProducer json = mock(ResultProducer.class);
        ForgeExchangeResult rez = new ForgeExchangeResult(1, "");
        when(json.produce(anyString(), any(Class.class))).thenReturn(rez);

        ExchangeImpl<ForgeExchangeResult> impl = new ExchangeImpl<ForgeExchangeResult>(req, json, ForgeExchangeResult.class);

        ForgeExchangeResult rez2 = impl.createResult("");

        assertTrue(rez == rez2);
    }


    @Test
    public void test_execute_cancelled() throws IOException {
        HttpUriRequest req = mock(HttpUriRequest.class);
        ResultProducer json = mock(ResultProducer.class);
        ForgeExchangeResult rez = new ForgeExchangeResult(1, "");
        when(json.produce(anyString(), any(Class.class))).thenReturn(rez);

        ExchangeImpl<ForgeExchangeResult> impl = new ExchangeImpl<ForgeExchangeResult>(req, json, ForgeExchangeResult.class);
        impl.cancel();

        HttpFunctionality http = mock(HttpFunctionality.class);
        ForgeExchangeResult rez2 = impl.execute(http);

        assertTrue(rez2 == null);
    }


    @Test(expected = IllegalStateException.class)
    public void test_execute_executed() throws IOException {
        HttpUriRequest req = mock(HttpUriRequest.class);
        when(req.getURI()).thenReturn(URI.create("http://somehost.com/"));
        ResultProducer json = mock(ResultProducer.class);
        ForgeExchangeResult rez = new ForgeExchangeResult(1, "");
        when(json.produce(anyString(), any(Class.class))).thenReturn(rez);

        ExchangeImpl<ForgeExchangeResult> impl = new ExchangeImpl<ForgeExchangeResult>(req, json, ForgeExchangeResult.class);

        HttpFunctionality http = mock(HttpFunctionality.class);
        when(http.execute(req)).thenReturn("some string");
        impl.execute(http);
        impl.execute(http);
    }


    @Test
    public void test_execute() throws IOException {
        HttpUriRequest req = mock(HttpUriRequest.class);
        when(req.getURI()).thenReturn(URI.create("http://somehost.com/"));
        ResultProducer json = mock(ResultProducer.class);
        ForgeExchangeResult rez = new ForgeExchangeResult(1, "");
        when(json.produce(anyString(), any(Class.class))).thenReturn(rez);

        ExchangeImpl<ForgeExchangeResult> impl = new ExchangeImpl<ForgeExchangeResult>(req, json, ForgeExchangeResult.class);

        HttpFunctionality http = mock(HttpFunctionality.class);
        when(http.execute(req)).thenReturn("some string");
        ForgeExchangeResult rez2 = impl.execute(http);

        assertTrue(rez == rez2);
    }
}