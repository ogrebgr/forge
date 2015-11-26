package com.bolyartech.forge.http.request;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import forge.apache.http.NameValuePair;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by ogre on 2015-10-12
 */
public class PostRequestBuilderTest {
    private static final String KEY1 = "key1";
    private static final String VALUE1 = "value1";

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();


    @Test
    public void test_addPostParameter() {
        PostRequestBuilder prb = new PostRequestBuilder("http://somehost.com/somepage.php");
        prb.postParameter(KEY1, VALUE1);

        ArrayList<NameValuePair> ret = prb.getPostParams();
        NameValuePair first = ret.get(0);
        assertTrue("'first' is null", first != null);

        assertTrue("key name different", first.getName().equals(KEY1));
        assertTrue("value different", first.getValue().equals(VALUE1));
    }


    @Test
    public void test_isPostParameterPresent() {
        PostRequestBuilder prb = new PostRequestBuilder("http://somehost.com:123/somepage.php?key1=value1");
        prb.postParameter(KEY1, VALUE1);
        assertTrue(prb.isPostParameterPresent(KEY1));
    }


    /**
     * Test if same named post parameter and file are added (which is unacceptable)
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_isPostParameterPresent2() throws IOException {
        PostRequestBuilder prb = new PostRequestBuilder("http://somehost.com:123/somepage.php?key1=value1");

        File f1 = tempFolder.newFile();
        prb.fileToUpload(KEY1, f1);


        prb.postParameter(KEY1, VALUE1);
        assertTrue(prb.isPostParameterPresent(KEY1));
    }


    @Test
    public void test_isPostParameterMissing() {
        PostRequestBuilder prb = new PostRequestBuilder("http://somehost.com:123/somepage.php?key1=value1");
        assertFalse(prb.isPostParameterPresent(KEY1));
    }


    /**
     * Test if same named post parameter and file are added (which is unacceptable)
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_addFileToUpload1() {
        PostRequestBuilder prb = new PostRequestBuilder("http://somehost.com:123/somepage.php?key1=value1");

        prb.postParameter(KEY1, VALUE1);

        File f1 = new File("aa");
        prb.fileToUpload(KEY1, f1);
    }


    /**
     * On nonexistent file exception should be thrown
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_addFileToUpload2() {
        PostRequestBuilder prb = new PostRequestBuilder("http://somehost.com:123/somepage.php?key1=value1");

        File f1 = new File("aa");
        prb.fileToUpload(KEY1, f1);
    }


    @Test
    public void test_addFileToUpload3() throws IOException {
        PostRequestBuilder prb = new PostRequestBuilder("http://somehost.com:123/somepage.php?key1=value1");

        File f1 = tempFolder.newFile();
        prb.fileToUpload(KEY1, f1);

        File f2 = prb.getFilesToUpload().get(KEY1);
        assertTrue(f1 == f2);
    }


    @Test
    public void test_construtor() {
        PostRequestBuilder prb = new PostRequestBuilder("http://somehost.com:123/somepage.php?key1=value1");
        assertTrue("protocol is different", prb.getProtocol().equals("http"));
        assertTrue("domain is different", prb.getDomain().equals("somehost.com"));
        assertTrue("path is different", prb.getPath().equals("/somepage.php"));
        assertTrue("port is different", prb.getPort() == 123);

        List<NameValuePair> ret = prb.getGetParams();
        NameValuePair first = ret.get(0);

        assertTrue("'first' is null", first != null);

        assertTrue("key name different", first.getName().equals(KEY1));
        assertTrue("value different", first.getValue().equals(VALUE1));
    }
}
