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


import okhttp3.Response;


/**
 * Result producer convert HTTP response into a result object of type <code>T</code>
 *
 * @param <T> Type of the result
 */
public interface ResultProducer<T> {
    /**
     * Creates object <code>T</code> from JSON string
     *
     * @param resp Response
     * @return The newly created object of type T
     * @throws ResultProducerException If the string cannot be processed to produce result of type T
     */
    @SuppressWarnings("RedundantThrows")
    T produce(Response resp) throws ResultProducerException;


    @SuppressWarnings("serial")
    class ResultProducerException extends Exception {

        /**
         * Creates new ResultProducerException
         */
        @SuppressWarnings("unused")
        public ResultProducerException() {
            super();
        }


        @SuppressWarnings({"JavaDoc"})
        public ResultProducerException(String message, Throwable cause) {
            super(message, cause);
        }


        @SuppressWarnings("JavaDoc")
        public ResultProducerException(String message) {
            super(message);
        }


        @SuppressWarnings({"unused", "JavaDoc"})
        public ResultProducerException(Throwable cause) {
            super(cause);
        }
    }
}
