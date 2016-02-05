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

package com.bolyartech.forge.http.functionality;

import forge.apache.http.HttpResponse;
import forge.apache.http.client.ResponseHandler;
import forge.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;


/**
 * Defines interface for executing HTTP requests
 */
public interface HttpFunctionality {
    /**
     * Executes HTTP request and returns the request body as String
     *
     * @param httpRequest HTTP request to be executed
     * @return String containing response body
     * @throws IOException If network error occurred
     */
    String execute(HttpUriRequest httpRequest) throws IOException;


    /**
     * Using custom response handler executes HTTP request.
     *
     * @param httpRequest     HTTP request to be executed
     * @param responseHandler Custom response handler with will create object of type {@code T} out of the HTTP response object
     * @param <T>             Type of the object which will be returned on success
     * @return Object of type {@code T}
     * @throws IOException If network error occurred
     */
    <T> T execute(HttpUriRequest httpRequest, ResponseHandler<T> responseHandler) throws IOException;


    /**
     * Executes HTTP request and returns the {@link HttpResponse} as returned by the {@code HttpClient}
     *
     * @param httpRequest HTTP request to be executed
     * @return HttpResponse
     * @throws IOException If network error occurred
     */
    HttpResponse executeForHttpResponse(HttpUriRequest httpRequest) throws IOException;


    /**
     * Shutdowns the functionality closing and freeing the used resources
     */
    void shutDown();
}
