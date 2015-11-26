package com.bolyartech.forge.http.misc;

import com.bolyartech.forge.http.functionality.HttpFunctionality;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import forge.apache.http.Header;
import forge.apache.http.HttpResponse;
import forge.apache.http.StatusLine;
import forge.apache.http.client.HttpClient;
import forge.apache.http.client.methods.HttpGet;
import forge.apache.http.client.methods.HttpUriRequest;
import forge.apache.http.entity.BasicHttpEntity;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by ogre on 2015-10-11
 */
public class FileDownloaderTest {
    private static final String CONTENT = "presni";

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();



    @Test
    public void test_downloadToFile() {

        HttpFunctionality httpFunc = mock(HttpFunctionality.class);

        Header contentEnc = mock(Header.class);
        when(contentEnc.getValue()).thenReturn("UTF-8");

        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(CONTENT.getBytes(StandardCharsets.UTF_8)));

        StatusLine sl = mock(StatusLine.class);
        when(sl.getStatusCode()).thenReturn(200);

        HttpResponse response = mock(HttpResponse.class);
        when(response.getStatusLine()).thenReturn(sl);
        when(response.getEntity()).thenReturn(entity);


        try {
            when(httpFunc.executeForHttpResponse(any(HttpUriRequest.class))).thenReturn(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        URI uri = URI.create("http://somehost.com/somepage.php");
        HttpGet get = new HttpGet(uri);
        try {
            File f = tempFolder.newFile();
            FileDownloader.download(httpFunc, get, f);
            String content = Files.toString(f, Charsets.UTF_8);

            assertTrue(CONTENT.equals(content));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
