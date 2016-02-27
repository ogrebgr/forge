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


import java.io.IOException;

/**
 * Created by ogre on 2015-10-11
 */


/**
 * Rest exchange
 * <p/>
 * Only single execution is allowed.
 *
 * @param <T> Type of the returned object
 */
public interface Exchange<T> {
    /**
     * Executes the exchange using the provided HttpFunctionality
     *
     * @return Object of type <code>T</code>
     * @throws IOException
     * @throws com.bolyartech.forge.base.exchange.ResultProducer.ResultProducerException
     */
    T execute() throws IOException, ResultProducer.ResultProducerException;

    /**
     * @return tag object
     */
    Object getTag();

    /**
     * @return if already executed <code>true</code>, <code>false</code> otherwise
     */
    boolean isExecuted();

    /**
     * If exchange is cancelled before it is executed will return <code>null</code>}.
     * If it is cancelled while waiting for HTTP request to complete it will return <code>null</code> no
     * matter if it was successful or not.
     */
    void cancel();

    /**
     * @return <code>true</code> if cancelled, <code>false</code> otherwise
     */
    boolean isCancelled();
}
