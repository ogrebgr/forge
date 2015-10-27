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

public interface ExchangeManager<T> extends ExchangeFunctionality.Listener {
    /**
     * Generate unique ID for an exchange
     *
     * @return Generated ID
     */
    Long generateXId();

    /**
     * @param exchange Exchange
     * @param xId      use {@link ExchangeManager#generateXId()} first to get an ID and provide it here.
     *                 if you don't care about the id, provide null.
     */
    void executeExchange(final Exchange<T> exchange, Long xId);
}
