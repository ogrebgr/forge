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

package com.bolyartech.forge.misc;

/**
 * Defines interface for creating object from JSON strings and vice versa
 */
public interface JsonFunctionality {
    /**
     * Creates object <code>T</code> from JSON string
     *
     * @param str   The string that contains json
     * @param clazz Class of the object to create
     * @return The newly created object of type clazz
     * @throws JsonParseException If the string is not valid JSON
     */
    <T> T fromJson(String str, Class<T> clazz) throws JsonParseException;


    /**
     * Converts java object to JSON string
     *
     * @param src Object to be represented as JSON
     * @return JSON representation of the object
     */
    String toJson(Object src);


    @SuppressWarnings("serial")
    class JsonParseException extends RuntimeException {

        public JsonParseException() {
            super();
        }


        public JsonParseException(String message, Throwable cause) {
            super(message, cause);
        }


        public JsonParseException(String message) {
            super(message);
        }


        public JsonParseException(Throwable cause) {
            super(cause);
        }
    }
}
