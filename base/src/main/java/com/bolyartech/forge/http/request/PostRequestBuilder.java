package com.bolyartech.forge.http.request;

import java.io.File;


/**
 * Created by ogre on 2015-10-11
 */
public interface PostRequestBuilder extends RequestBuilder {
    /**
     * Adds POST parameter. Please note that {@link #addFileToUpload(String, File) is also adding POST parameter}
     *
     * @param key   Name of the parameter
     * @param value Value of the parameter
     */
    void addPostParameter(String key, String value);


    /**
     * Checks if post parameter is already present
     *
     * @param key Name of the parameter
     * @return <code>true</code> if present, <code>false</code> otherwise
     */
    boolean isPostParameterPresent(String key);

    /**
     * Adds file to be uploaded. This is added as "special" post parameter (that is as MultipartEntity)
     *
     * @param paramName name of the parameter
     * @param file      File to be uploaded
     */
    void addFileToUpload(String paramName, File file);
}
