package com.bolyartech.forge.rest;


import com.bolyartech.forge.http.functionality.HttpFunctionality;
import com.bolyartech.forge.misc.JsonFunctionality;
import forge.apache.http.client.methods.HttpUriRequest;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


/**
 * Created by ogre on 2015-10-14
 */
public class RestExchangeImplTest {
    @Test(expected = NullPointerException.class)
    public void test_constructor1() {
        RestExchangeImpl impl = new RestExchangeImpl(null, null, null);
    }


    @Test(expected = NullPointerException.class)
    public void test_constructor2() {
        HttpUriRequest req = mock(HttpUriRequest.class);
        RestExchangeImpl<KhRestResult> impl = new RestExchangeImpl<>(req, null, null);
    }


    @Test(expected = NullPointerException.class)
    public void test_constructor3() {
        HttpUriRequest req = mock(HttpUriRequest.class);
        JsonFunctionality json = mock(JsonFunctionality.class);
        RestExchangeImpl<KhRestResult> impl = new RestExchangeImpl<>(req, json, null);
    }


    /**
     * Tests if class will NOT throw NullPointerException when all parameters are present
     */
    @Test
    public void test_constructor4() {
        HttpUriRequest req = mock(HttpUriRequest.class);
        JsonFunctionality json = mock(JsonFunctionality.class);

        RestExchangeImpl<KhRestResult> impl = new RestExchangeImpl<>(req, json, KhRestResult.class);

        assertTrue(true);
    }


    @Test
    public void test_createResult() {
        HttpUriRequest req = mock(HttpUriRequest.class);
        JsonFunctionality json = mock(JsonFunctionality.class);
        KhRestResult rez = new KhRestResult(1, "");
        when(json.fromJson(anyString(), any(Class.class))).thenReturn(rez);

        RestExchangeImpl<KhRestResult> impl = new RestExchangeImpl<KhRestResult>(req, json, KhRestResult.class);

        KhRestResult rez2 = impl.createResult("");

        assertTrue(rez == rez2);
    }


    @Test
    public void test_execute_cancelled() throws IOException {
        HttpUriRequest req = mock(HttpUriRequest.class);
        JsonFunctionality json = mock(JsonFunctionality.class);
        KhRestResult rez = new KhRestResult(1, "");
        when(json.fromJson(anyString(), any(Class.class))).thenReturn(rez);

        RestExchangeImpl<KhRestResult> impl = new RestExchangeImpl<KhRestResult>(req, json, KhRestResult.class);
        impl.cancel();

        HttpFunctionality http = mock(HttpFunctionality.class);
        KhRestResult rez2 = impl.execute(http);

        assertTrue(rez2 == null);
    }


    @Test(expected = IllegalStateException.class)
    public void test_execute_executed() throws IOException {
        HttpUriRequest req = mock(HttpUriRequest.class);
        when(req.getURI()).thenReturn(URI.create("http://somehost.com/"));
        JsonFunctionality json = mock(JsonFunctionality.class);
        KhRestResult rez = new KhRestResult(1, "");
        when(json.fromJson(anyString(), any(Class.class))).thenReturn(rez);

        RestExchangeImpl<KhRestResult> impl = new RestExchangeImpl<KhRestResult>(req, json, KhRestResult.class);

        HttpFunctionality http = mock(HttpFunctionality.class);
        when(http.execute(req)).thenReturn("some string");
        impl.execute(http);
        impl.execute(http);
    }


    @Test
    public void test_execute() throws IOException {
        HttpUriRequest req = mock(HttpUriRequest.class);
        when(req.getURI()).thenReturn(URI.create("http://somehost.com/"));
        JsonFunctionality json = mock(JsonFunctionality.class);
        KhRestResult rez = new KhRestResult(1, "");
        when(json.fromJson(anyString(), any(Class.class))).thenReturn(rez);

        RestExchangeImpl<KhRestResult> impl = new RestExchangeImpl<KhRestResult>(req, json, KhRestResult.class);

        HttpFunctionality http = mock(HttpFunctionality.class);
        when(http.execute(req)).thenReturn("some string");
        KhRestResult rez2 = impl.execute(http);

        assertTrue(rez == rez2);
    }
}