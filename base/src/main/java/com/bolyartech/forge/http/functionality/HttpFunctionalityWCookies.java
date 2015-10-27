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

package com.bolyartech.forge.http.functionality;

import forge.apache.http.cookie.Cookie;

import java.util.Date;


/**
 * HttpFunctionality with added cookie support
 */
public interface HttpFunctionalityWCookies extends HttpFunctionality {
    /**
     * Checks if cookie is present
     *
     * @param cookie Cookie to check if present
     * @return <code>true</code> if cookie is present, <code>false</code> otherwise
     */
    boolean cookieExists(Cookie cookie);


    /**
     * Checks if cookie is present. All 3 parameters must match
     *
     * @param name   Name of the cookie
     * @param domain Domain of the cookie
     * @param path   Path of the cookie
     * @return <code>true</code> if cookie is present, <code>false</code> otherwise
     */
    boolean cookieExists(String name, String domain, String path);


    /**
     * Sets cookie
     *
     * @param cookie Sets cookie
     */
    void setCookie(Cookie cookie);


    /**
     * Gets cookie value if present
     *
     * @param name   Name of the cookie
     * @param domain Domain of the cookie
     * @param path   Path of the cookie
     * @return Cookie value if cookie is present, null otherwise
     */
    String getCookieValue(String name, String domain, String path);


    /**
     * Returns the value of a first found cookie with that name
     *
     * @param name Name of the cookie
     * @return Value of the first found cookie with that name
     */
    String getCookieValue(String name);


    /**
     * Gets {@link Cookie} object if present
     *
     * @param name   Name of the cookie
     * @param domain Domain of the cookie
     * @param path   Path of the cookie
     * @return {@code Cookie} if present, null otherwise
     */
    Cookie getCookie(String name, String domain, String path);


    /**
     * Returns the first found {@link Cookie} with that name
     *
     * @param name Name of the cookie
     * @return {@code Cookie} if present, null otherwise
     */
    Cookie getCookie(String name);


    /**
     * Clears all cookies (cookie store becomes empty)
     */
    void clearCookies();


    /**
     * Clears expired cookies
     * <p/>
     * All cookies that have expiry date earlier than specified are removed.
     *
     * @param date Date to be used as end of the valid interval
     */
    void clearExpiredCookies(Date date);
}
