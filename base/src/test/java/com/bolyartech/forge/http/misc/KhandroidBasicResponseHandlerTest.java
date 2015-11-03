package com.bolyartech.forge.http.misc;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import forge.apache.http.Header;
import forge.apache.http.HttpEntity;
import forge.apache.http.HttpResponse;
import forge.apache.http.StatusLine;
import forge.apache.http.impl.client.BasicResponseHandler;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by ogre on 2015-10-11
 */
public class KhandroidBasicResponseHandlerTest {
    private static final String CONTENT = "presni";


    @Test
    public void test_handleResponse() {
        Header contentEnc = mock(Header.class);
        when(contentEnc.getValue()).thenReturn("UTF-8");

        HttpEntity entity = mock(HttpEntity.class);
        try {
            when(entity.getContentEncoding()).thenReturn(contentEnc);
            when(entity.getContentLength()).thenReturn(6l);
            when(entity.getContent()).thenReturn(new ByteArrayInputStream(CONTENT.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StatusLine sl = mock(StatusLine.class);
        when(sl.getStatusCode()).thenReturn(200);

        HttpResponse response = mock(HttpResponse.class);
        when(response.getStatusLine()).thenReturn(sl);
        when(response.getEntity()).thenReturn(entity);

        BasicResponseHandler test = new BasicResponseHandlerImpl();
        try {
            String ret = test.handleResponse(response);
            assertTrue(ret.equals(CONTENT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
