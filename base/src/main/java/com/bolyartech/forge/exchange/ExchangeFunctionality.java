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


public interface ExchangeFunctionality<T> {
    /**
     * Starts the ExchangeFunctionality
     */
    void start();

    /**
     * Adds a listener
     *
     * @param listener Listener to be added
     */
    void addListener(Listener<T> listener);

    /**
     * Removes a listener
     *
     * @param listener Listener to be removed
     */
    void removeListener(Listener<T> listener);

    /**
     * Executes {@link Exchange} with default TTL
     *
     * @param x   Exchange
     * @param xId ID of the exchange (use {@link #generateXId()} to generate ID
     */
    void executeExchange(final Exchange<T> x, Long xId);

    /**
     * Executes {@link Exchange} with specified TTL
     *
     * @param x   Exchange
     * @param xId ID of the exchange (use {@link #generateXId()} to generate ID
     * @param ttl TTL for the exchange
     */
    void executeExchange(final Exchange<T> x, Long xId, long ttl);

    /**
     * Generates unique ID for the exchange. Implementations of ExchangeFunctionality
     * must provide thread safe implementation of this method.
     *
     * @return Generated ID
     */
    Long generateXId();

    /**
     * Shutdowns the ExchangeFunctionality
     */
    void shutdown();

    /**
     * Checks if this ExchangeFunctionality is started
     *
     * @return <code>true</code> if started, <code>false</code> otherwise
     */
    boolean isStarted();

    /**
     * Checks if this ExchangeFunctionality is shutdown
     *
     * @return <code>true</code> if shutdown, <code>false</code> otherwise
     */
    boolean isShutdown();

    /**
     * Listener for completed exchanges
     */
    interface Listener<T> {
        /**
         * Called then exchange is completed (either successful or not)
         *
         * @param out Outcome of the exchange
         * @param idL ID of the exchange
         */
        void onExchangeCompleted(ExchangeOutcome<T> out, long idL);
    }
}
