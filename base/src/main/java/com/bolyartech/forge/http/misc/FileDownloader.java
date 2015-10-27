/*
 * Copyright (C) 2012-2015 Ognyan Bankov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bolyartech.forge.http.misc;

import forge.apache.http.HttpEntity;
import forge.apache.http.HttpResponse;
import forge.apache.http.StatusLine;
import forge.apache.http.client.HttpClient;
import forge.apache.http.client.methods.HttpGet;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;

import static com.google.common.io.ByteStreams.copy;


/**
 * Helper class for HTTP file downloads
 */
public class FileDownloader {
    private static final org.slf4j.Logger mLogger = LoggerFactory
            .getLogger(FileDownloader.class.getSimpleName());


    /**
     * Downloads file to a byte array
     *
     * @param httpClient HttpClient implementation
     * @param source     URI of the file to be downloaded
     * @return downloaded file as byte array
     * @throws IOException if network error occurs
     */
    public static byte[] download(HttpClient httpClient, URI source) throws
            IOException {
        byte[] ret;

        mLogger.trace("Downloading " + source.toString());
        HttpGet req = new HttpGet(source);
        HttpResponse response = httpClient.execute(req);
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        mLogger.trace("Status code:" + statusCode);
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            entity.writeTo(output);
            output.close();

            ret = output.toByteArray();
        } else {
            throw new IOException("Download failed, HTTP response code " + statusCode + " - "
                    + statusLine.getReasonPhrase());
        }
        req.releaseConnection();

        return ret;
    }


    /**
     * Downloads remote file via HTTP to local file. Internally this method
     * uses {@link #download(HttpClient, URI)} so be aware when downloading
     * large files that they will be first put in memory in a byte[] (which may
     * lead to problems on Android for files larger that 5MB).
     *
     * @param httpClient  HttpClient implementation
     * @param source      URI of the file to be downloaded
     * @param destination Destination file
     * @throws IOException if network error occurs
     */
    public static void download(HttpClient httpClient,
                                URI source,
                                File destination
    ) throws IOException {
        byte[] content = download(httpClient, source);
        ByteArrayInputStream input = new ByteArrayInputStream(content);
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(destination));

        copy(input, output);
        input.close();
        output.close();
    }
}
