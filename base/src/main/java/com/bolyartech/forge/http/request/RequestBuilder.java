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
package com.bolyartech.forge.http.request;

import forge.apache.http.client.methods.HttpUriRequest;

/**
 * Created by ogre on 2015-10-11
 */


/**
 * Defines interface fo request builder
 */
public interface RequestBuilder {
    /**
     * Builds the request
     *
     * @return Builded request
     */
    HttpUriRequest build();

    /**
     * Adds GET parameter
     *
     * @param key   Name of the parameter
     * @param value Value of the parameter
     */
    void addParameter(String key, String value);

    /**
     * Checks if parameter is already added
     *
     * @param key Name of the parameter
     * @return <code>true</code> if present, <code>false</code> otherwise
     */
    boolean isParameterPresent(String key);
}
