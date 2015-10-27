/*
 * Copyright (C) 2012-2013 Ognyan Bankov
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

package com.bolyartech.forge.http.misc;

import forge.apache.http.client.CookieStore;
import forge.apache.http.cookie.Cookie;

import java.util.Date;
import java.util.List;


/**
 * Thread-safe CookieStore. This class is just a wrapper around CookieStore with all methods synchronized.
 */
public class SynchronizedCookieStore implements CookieStore {
    private final CookieStore mStore;


    /**
     * Creates SynchronizedCookieStore
     *
     * @param store CookieStore to be wrapped
     */
    public SynchronizedCookieStore(CookieStore store) {
        super();
        mStore = store;
    }


    @Override
    public synchronized void addCookie(Cookie cookie) {
        mStore.addCookie(cookie);
    }


    @Override
    public synchronized void clear() {
        mStore.clear();
    }


    @Override
    public synchronized boolean clearExpired(Date date) {
        return mStore.clearExpired(date);
    }


    @Override
    public synchronized List<Cookie> getCookies() {
        return mStore.getCookies();
    }
}
