package com.bolyartech.forge.http.request;

import org.junit.Test;

import java.util.ArrayList;

import forge.apache.http.NameValuePair;

import static org.junit.Assert.assertTrue;


/**
 * Created by ogre on 2015-10-11
 */
public class GetRequestBuilderImplTest {
    private static final String KEY1 = "key1";
    private static final String VALUE1 = "value1";


    @Test
    public void test_construtor() {
        GetRequestBuilder grb = new GetRequestBuilder("http://somehost.com:123/somepage.php?key1=value1");
        assertTrue("protocol is different", grb.getProtocol().equals("http"));
        assertTrue("domain is different", grb.getDomain().equals("somehost.com"));
        assertTrue("path is different", grb.getPath().equals("/somepage.php"));
        assertTrue("port is different", grb.getPort() == 123);

        ArrayList<NameValuePair> ret = grb.getGetParams();
        NameValuePair first = ret.get(0);

        assertTrue("'first' is null", first != null);

        assertTrue("key name different", first.getName().equals(KEY1));
        assertTrue("value different", first.getValue().equals(VALUE1));
    }


    @Test
    public void test_addParameter() {
        GetRequestBuilder grb = new GetRequestBuilder("http://somehost.com/somepage.php");
        grb.parameter(KEY1, VALUE1);

        ArrayList<NameValuePair> ret = grb.getGetParams();
        NameValuePair first = ret.get(0);

        assertTrue("'first' is null", first != null);

        assertTrue("key name different", first.getName().equals(KEY1));
        assertTrue("value different", first.getValue().equals(VALUE1));
    }


    /**
     * Tests if get parameters are parsed and duplicate get parameter is added
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_isParameterPresent() {
        GetRequestBuilder grb = new GetRequestBuilder("http://somehost.com:123/somepage.php?key1=value1");
        grb.parameter(KEY1, VALUE1);
    }


    @Test
    public void test_isParameterPresent2() {
        GetRequestBuilder grb = new GetRequestBuilder("http://somehost.com:123/somepage.php");
        grb.parameter(KEY1, VALUE1);
        assertTrue(grb.isParameterPresent(KEY1));
    }
}
