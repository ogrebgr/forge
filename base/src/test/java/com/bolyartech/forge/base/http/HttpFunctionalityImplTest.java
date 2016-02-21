package com.bolyartech.forge.base.http;


import okhttp3.*;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;


public class HttpFunctionalityImplTest {
    @Test
    public void testHttpFunctionalityImpl() throws IOException {
        OkHttpClient ok = mock(OkHttpClient.class);

        Request req = new Request.Builder().url("http://test").build();
        Response resp = new Response.Builder().
                request(req).
                protocol(Protocol.HTTP_1_0).
                code(200).
                build();

        Call call = mock(Call.class);
        when(call.execute()).thenReturn(resp);
        when(ok.newCall(req)).thenReturn(call);

        HttpFunctionalityImpl http = new HttpFunctionalityImpl(ok);
        Response actual = http.execute(req);
        assertTrue("Unexpected response", actual == resp);
    }

}
