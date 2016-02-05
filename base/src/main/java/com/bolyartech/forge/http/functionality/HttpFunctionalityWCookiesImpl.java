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

package com.bolyartech.forge.http.functionality;

import com.bolyartech.forge.http.ForgeCloseableHttpClient;
import com.bolyartech.forge.misc.ForUnitTestsOnly;
import forge.apache.http.client.CookieStore;
import forge.apache.http.client.protocol.HttpClientContext;
import forge.apache.http.cookie.Cookie;

import java.util.Date;
import java.util.List;


public class HttpFunctionalityWCookiesImpl extends HttpFunctionalityImpl implements
        HttpFunctionalityWCookies {

    HttpClientContext mContext = HttpClientContext.create();
    private CookieStore mCookies;


    public HttpFunctionalityWCookiesImpl(ForgeCloseableHttpClient httpClient, CookieStore cookies) {
        super(httpClient);

        if (cookies != null) {
            mCookies = cookies;
            mContext.setCookieStore(cookies);
        } else {
            throw new NullPointerException("Parameter cookies is null");
        }
    }


    @Override
    public boolean cookieExists(Cookie cookie) {
        boolean ret = false;

        List<Cookie> l = mCookies.getCookies();
        for (Cookie c : l) {
            if (c.equals(cookie)) {
                ret = true;
                break;
            }
        }

        return ret;
    }


    /**
     * Adds new cookie or replaces existing
     *
     * @param cookie Cookie
     */
    @Override
    public void setCookie(Cookie cookie) {
        mCookies.addCookie(cookie);
    }


    @Override
    public boolean cookieExists(String name, String domain, String path) {
        boolean ret = false;

        List<Cookie> l = mCookies.getCookies();
        for (Cookie c : l) {
            if (c.getName().equals(name) && c.getDomain().equals(domain)
                    && c.getPath().equals(path)) {
                ret = true;
                break;
            }
        }

        return ret;
    }


    public Cookie getCookie(String name, String domain, String path) {
        Cookie ret = null;

        List<Cookie> l = mContext.getCookieStore().getCookies();
        for (Cookie c : l) {
            if (c.getName().equals(name) && c.getDomain().equals(domain)
                    && c.getPath().equals(path)) {
                ret = c;
                break;
            }
        }

        return ret;
    }


    /**
     * Returns first cookie with the given name
     * <p/>
     * Usually you work with one and the same site so all of the cookies have same domain and path.
     * In such case it is save to retrieve cookie just by name.
     * However, if you work with several sites/paths this method is not safe because it will return the first cookie
     * with the given name which may not be what you want.
     *
     * @param name Name of the cookie
     * @return Value of the cookie or <code>null</code> if not found
     */
    public Cookie getCookie(String name) {
        Cookie ret = null;

        List<Cookie> l = mContext.getCookieStore().getCookies();
        for (Cookie c : l) {
            if (c.getName().equals(name)) {
                ret = c;
                break;
            }
        }

        return ret;
    }


    public String getCookieValue(String name, String domain, String path) {
        String ret = null;

        Cookie c = getCookie(name, domain, path);
        if (c != null) {
            ret = c.getValue();
        }

        return ret;
    }


    /**
     * Not safe. @see #getCookie(String)
     */
    public String getCookieValue(String name) {
        String ret = null;

        Cookie c = getCookie(name);
        if (c != null) {
            ret = c.getValue();
        }

        return ret;
    }


    @Override
    public void clearCookies() {
        mCookies.clear();
    }


    @Override
    public void clearExpiredCookies(Date date) {
        mCookies.clearExpired(date);
    }


    @ForUnitTestsOnly
    CookieStore getCookieStore() {
        return mCookies;
    }
}
