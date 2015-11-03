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
package com.bolyartech.forge.exchange;

/**
 * Created by ogre on 2015-11-03 13:03
 */
public interface ResultProducer<T> {
    /**
     * Creates object <code>T</code> from JSON string
     *
     * @param str   The string that contains resultProducer
     * @param clazz Class of the object to create
     * @return The newly created object of type clazz
     * @throws ResultProducerException If the string cannot be processed to produce result of type T
     */
    T produce(String str, Class<T> clazz) throws ResultProducerException;


    @SuppressWarnings("serial")
    class ResultProducerException extends RuntimeException {

        public ResultProducerException() {
            super();
        }


        public ResultProducerException(String message, Throwable cause) {
            super(message, cause);
        }


        public ResultProducerException(String message) {
            super(message);
        }


        public ResultProducerException(Throwable cause) {
            super(cause);
        }
    }
}
