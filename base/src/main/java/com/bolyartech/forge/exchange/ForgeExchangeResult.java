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

package com.bolyartech.forge.exchange;


/**
 * Result of a successful {@link Exchange}
 * <p/>
 * Thread safety: full (immutable object)
 */
public class ForgeExchangeResult {
    /**
     * Result code. Usually positive codes indicate success, negative - some "soft" failure
     */
    private final int code;
    /**
     * Payload of the result. Usually JSON encoded string
     */
    private final String payload;


    /**
     * Creates new ForgeExchangeResult
     *
     * @param code    Code of the result
     * @param payload Payload of result
     */
    public ForgeExchangeResult(int code, String payload) {
        super();
        this.code = code;
        this.payload = payload;
    }


    /**
     * @return code of the result
     */
    public int getCode() {
        return code;
    }


    /**
     * @return payload of the result
     */
    public String getPayload() {
        return payload;
    }


    @Override
    public String toString() {
        return "Code: " + code + " Payload: " + payload;
    }
}
