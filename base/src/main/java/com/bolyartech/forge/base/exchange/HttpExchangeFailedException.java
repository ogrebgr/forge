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

package com.bolyartech.forge.base.exchange;

/**
 * Exception used to signal that Exchange has failed
 */
@SuppressWarnings("serial")
public class HttpExchangeFailedException extends Exception {

    /**
     * Creates new HttpExchangeFailedException
     */
    @SuppressWarnings("unused")
    public HttpExchangeFailedException() {
        super();
    }


    /**
     * Creates new HttpExchangeFailedException
     */
    @SuppressWarnings("unused")
    public HttpExchangeFailedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }


    /**
     * Creates new HttpExchangeFailedException
     */
    @SuppressWarnings("unused")
    public HttpExchangeFailedException(String detailMessage) {
        super(detailMessage);
    }


    /**
     * Creates new HttpExchangeFailedException
     */
    @SuppressWarnings("unused")
    public HttpExchangeFailedException(Throwable throwable) {
        super(throwable);
    }

}
