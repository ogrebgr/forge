package com.bolyartech.forge.http.request;

import forge.apache.http.client.methods.HttpUriRequest;

/**
 * Created by ogre on 2015-10-11
 */


/**
 * Defines interface fo request builder
 */
public interface RequestBuilder {
    /**
     * Builds the request
     *
     * @return Builded request
     */
    HttpUriRequest build();

    /**
     * Adds GET parameter
     *
     * @param key   Name of the parameter
     * @param value Value of the parameter
     */
    void addParameter(String key, String value);

    /**
     * Checks if parameter is already added
     *
     * @param key Name of the parameter
     * @return <code>true</code> if present, <code>false</code> otherwise
     */
    boolean isParameterPresent(String key);
}
