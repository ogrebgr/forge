package com.bolyartech.forge.http.functionality;


import com.bolyartech.forge.http.ForgeCloseableHttpClient;
import com.bolyartech.forge.http.misc.BasicResponseHandlerImpl;

import org.junit.Test;

import java.io.IOException;


import forge.apache.http.HttpResponse;
import forge.apache.http.client.methods.CloseableHttpResponse;
import forge.apache.http.client.methods.HttpUriRequest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by ogre on 2015-10-12
 */
public class HttpFunctionalityImplTest {

    @Test
    public void test_constructor() {
        ForgeCloseableHttpClient httpClient = mock(ForgeCloseableHttpClient.class);
        HttpFunctionalityImpl impl = new HttpFunctionalityImpl(httpClient);
    }


    @Test
    public void test_execute1() throws IOException {
        ForgeCloseableHttpClient httpClient = mock(ForgeCloseableHttpClient.class);

        HttpUriRequest req = mock(HttpUriRequest.class);

        HttpFunctionalityImpl impl = new HttpFunctionalityImpl(httpClient);
        impl.execute(req);

        verify(httpClient).execute(any(HttpUriRequest.class), any(BasicResponseHandlerImpl.class));
    }


    @Test
    public void test_execute2() throws IOException {
        CloseableHttpResponse resp = mock(CloseableHttpResponse.class);

        HttpUriRequest req = mock(HttpUriRequest.class);

        ForgeCloseableHttpClient httpClient = mock(ForgeCloseableHttpClient.class);
        when(httpClient.execute(req)).thenReturn(resp);


        HttpFunctionalityImpl impl = new HttpFunctionalityImpl(httpClient);
        HttpResponse resp2 = impl.executeForHttpResponse(req);

        assertTrue(resp == resp2);
    }
}
