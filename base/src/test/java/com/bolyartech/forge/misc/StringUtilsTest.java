package com.bolyartech.forge.misc;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by ogre on 2015-10-11
 */
public class StringUtilsTest {
    @Test
    public void test_md5() {
        assertTrue("md5(String) not working correctly",
                StringUtils.md5("presni").equals("38b25651b46ef30721cb7f9091831bb0"));
    }


    @Test
    public void test_md5Int() {
        assertTrue("md5(int) not working correctly",
                StringUtils.md5(5).equals("e4da3b7fbbce2345d7772b0674a318d5"));
    }


    @Test
    public void test_md5Long() {
        assertTrue("md5(long) not working correctly",
                StringUtils.md5(5l).equals("e4da3b7fbbce2345d7772b0674a318d5"));
    }


    @Test
    public void test_isEmpty() {
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty("aa"));
    }


    @Test
    public void test_isNotEmpty() {
        assertTrue(StringUtils.isNotEmpty("aa"));
        assertFalse(StringUtils.isNotEmpty(""));
    }

}
