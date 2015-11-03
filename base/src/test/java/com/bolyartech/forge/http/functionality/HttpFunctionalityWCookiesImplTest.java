package com.bolyartech.forge.http.functionality;


import com.bolyartech.forge.http.ForgeCloseableHttpClient;

import org.junit.Test;

import java.util.List;


import forge.apache.http.client.CookieStore;
import forge.apache.http.cookie.Cookie;
import forge.apache.http.impl.client.BasicCookieStore;
import forge.apache.http.impl.cookie.BasicClientCookie;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


/**
 * Created by ogre on 2015-10-12
 */
public class HttpFunctionalityWCookiesImplTest {
    @Test
    public void test_constructor() {
        ForgeCloseableHttpClient httpClient = mock(ForgeCloseableHttpClient.class);
        CookieStore cookieStore = mock(CookieStore.class);

        HttpFunctionalityWCookiesImpl impl = new HttpFunctionalityWCookiesImpl(httpClient, cookieStore);

        assertTrue(impl.getCookieStore() == cookieStore);
    }


    @Test
    public void test_setCookie() {
        ForgeCloseableHttpClient httpClient = mock(ForgeCloseableHttpClient.class);
        CookieStore cookieStore = new BasicCookieStore();

        BasicClientCookie cookie = new BasicClientCookie("test", "1");

        HttpFunctionalityWCookiesImpl impl = new HttpFunctionalityWCookiesImpl(httpClient, cookieStore);
        impl.setCookie(cookie);

        CookieStore cs = impl.getCookieStore();
        List<Cookie> cl = cs.getCookies();
        Cookie c = cl.get(0);
        assertTrue(cookie == c);
    }


    @Test
    public void test_getCookie1() {
        ForgeCloseableHttpClient httpClient = mock(ForgeCloseableHttpClient.class);
        CookieStore cookieStore = new BasicCookieStore();

        BasicClientCookie cookie = new BasicClientCookie("test", "1");

        HttpFunctionalityWCookiesImpl impl = new HttpFunctionalityWCookiesImpl(httpClient, cookieStore);
        Cookie c1 = impl.getCookie("testother");
        assertTrue(c1 == null);


        impl.setCookie(cookie);

        Cookie c2 = impl.getCookie("test");
        assertTrue(cookie == c2);
    }


    @Test
    public void test_getCookie2() {
        ForgeCloseableHttpClient httpClient = mock(ForgeCloseableHttpClient.class);
        CookieStore cookieStore = new BasicCookieStore();

        BasicClientCookie cookie = new BasicClientCookie("test", "1");
        cookie.setDomain("somehost.com");
        cookie.setPath("/somepath/");

        HttpFunctionalityWCookiesImpl impl = new HttpFunctionalityWCookiesImpl(httpClient, cookieStore);
        impl.setCookie(cookie);

        Cookie c1 = impl.getCookie("testother", "somehost.com", "/somepath/");
        assertTrue(c1 == null);

        Cookie c2 = impl.getCookie("test", "somehost.com", "/somepath/");
        assertTrue(cookie == c2);
    }


    @Test
    public void test_cookieExists() {
        ForgeCloseableHttpClient httpClient = mock(ForgeCloseableHttpClient.class);
        CookieStore cookieStore = new BasicCookieStore();

        BasicClientCookie cookie = new BasicClientCookie("test", "1");
        cookie.setDomain("somehost.com");
        cookie.setPath("/somepath/");

        HttpFunctionalityWCookiesImpl impl = new HttpFunctionalityWCookiesImpl(httpClient, cookieStore);
        assertFalse(impl.cookieExists("testother", "somehost.com", "/somepath/"));

        impl.setCookie(cookie);
        assertTrue(impl.cookieExists("test", "somehost.com", "/somepath/"));

        assertTrue(impl.cookieExists(cookie));
    }


    @Test
    public void test_getCookieValue() {
        ForgeCloseableHttpClient httpClient = mock(ForgeCloseableHttpClient.class);
        CookieStore cookieStore = new BasicCookieStore();

        BasicClientCookie cookie = new BasicClientCookie("test", "1");
        cookie.setDomain("somehost.com");
        cookie.setPath("/somepath/");

        HttpFunctionalityWCookiesImpl impl = new HttpFunctionalityWCookiesImpl(httpClient, cookieStore);
        impl.setCookie(cookie);

        assertTrue(impl.getCookieValue("test").equals("1"));
        assertTrue(impl.getCookieValue("test", "somehost.com", "/somepath/").equals("1"));
    }


    @Test
    public void test_clearCookies() {
        ForgeCloseableHttpClient httpClient = mock(ForgeCloseableHttpClient.class);
        CookieStore cookieStore = new BasicCookieStore();

        BasicClientCookie cookie = new BasicClientCookie("test", "1");
        cookie.setDomain("somehost.com");
        cookie.setPath("/somepath/");

        HttpFunctionalityWCookiesImpl impl = new HttpFunctionalityWCookiesImpl(httpClient, cookieStore);
        impl.setCookie(cookie);
        impl.clearCookies();

        CookieStore cs = impl.getCookieStore();
        assertTrue(cs.getCookies().size() == 0);
    }
}
