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
 * Represents outcome of a exchange
 *
 * @param <T> Type of the result of the exchange
 *            thread safety: full (this object is immutable)
 */
public class ExchangeOutcome<T> {
    private final Exchange<T> mExchange;
    private final T mResult;
    private final boolean mError;


    /**
     * Creates new ExchangeOutcome
     *
     * @param exchange {@link Exchange} that was executed
     * @param result   Result of the exchange of type <code>T</code> if exchange execution was successful
     * @param error    indicates if there was an error during exchange execution. If <code>true</code> may be <code>null</code>
     */
    public ExchangeOutcome(Exchange<T> exchange, T result, boolean error) {
        super();
        mExchange = exchange;
        mResult = result;
        mError = error;
    }


    /**
     * @return <code>true</code> if there was error in exchange execution, <code>false</code> otherwise
     */
    public boolean isError() {
        return mError;
    }


    /**
     * @return executed exchange
     */
    public Exchange<T> getExchange() {
        return mExchange;
    }


    /**
     * @return Result ot the exchange of type <code>T</code>
     */
    public T getResult() {
        return mResult;
    }
}