package com.bolyartech.forge.base.exchange;

import com.bolyartech.forge.base.test_utils.StringResultProducer;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpExchangeTest {
    @Test
    public void testExecute() throws IOException, ResultProducer.ResultProducerException {
        OkHttpClient ok = mock(OkHttpClient.class);

        Request req = new Request.Builder().url("http://test").build();

        String respBody = "it is working";
        Response resp = new Response.Builder().
                request(req).
                protocol(Protocol.HTTP_1_0).
                code(200).
                body(ResponseBody.create(MediaType.parse("text/plain"), respBody)).
                message("gg").
                build();

        Call call = mock(Call.class);
        when(call.execute()).thenReturn(resp);
        when(ok.newCall(req)).thenReturn(call);

        HttpExchange<String> x = new HttpExchange<>(ok, req, new StringResultProducer());
        String rez = x.execute();
        Assert.assertTrue("Unexpected result", rez.equals(respBody));
    }
}
