/*
 * Copyright (C) 2012-2016 Ognyan Bankov
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

import com.bolyartech.forge.http.functionality.HttpFunctionality;
import com.bolyartech.forge.http.request.ProgressListener;

import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import forge.apache.http.HttpEntity;
import forge.apache.http.HttpResponse;
import forge.apache.http.StatusLine;
import forge.apache.http.client.methods.HttpGet;


/**
 * Helper class for HTTP file downloads. Not suitable for large files (when you will need to show progress indicator)
 */
public class FileDownloader {
    private static final org.slf4j.Logger mLogger = LoggerFactory
            .getLogger(FileDownloader.class.getSimpleName());


    /**
     * Downloads remote file via HTTP to local file. This method is blocking until the whole file is downloaded so it is good idea to call it off your main thread
     *
     * @param httpFunc       HttpFunctionality implementation
     * @param request           HttpGet to be executed to download
     * @param destination      Destination fil
     * @param progressListener Instance of {@link ProgressListener} to be used as listener
     * @throws IOException if network error occurs
     */
    public static HttpGet download(HttpFunctionality httpFunc,
                                   HttpGet request,
                                    File destination,
                                ProgressListener progressListener
    ) throws IOException {


        HttpResponse response = httpFunc.executeForHttpResponse(request);
        StatusLine statusLine = response.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        mLogger.trace("Status code:" + statusCode);
        if (statusCode == 200) {
            try {
                HttpEntity entity = response.getEntity();


                if (progressListener == null) {
                    BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(destination));
                    entity.writeTo(output);
                    output.close();
                } else {
                    BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(destination));
                    ProgressFilterOutputStream output = new ProgressFilterOutputStream(os, progressListener, entity.getContentLength());
                    entity.writeTo(output);
                    output.close();
                }
            } finally {
                request.releaseConnection();
            }
        } else {
            throw new IOException("Download failed, HTTP response code " + statusCode + " - "
                    + statusLine.getReasonPhrase());
        }

        return request;
    }


    /**
     * Downloads remote file via HTTP to local file. This method is blocking until the whole file is downloaded so it is good idea to call it off your main thread
     *
     * @param httpFunc       HttpFunctionality implementation
     * @param request           HttpGet to be executed to download
     * @param destination      Destination fil
     * @throws IOException if network error occurs
     */
    public static HttpGet download(HttpFunctionality httpFunc,
                                   HttpGet request,
                                File destination) throws IOException {

        return download(httpFunc, request, destination, null);
    }

}
