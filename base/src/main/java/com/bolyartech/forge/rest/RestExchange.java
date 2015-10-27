package com.bolyartech.forge.rest;


import com.bolyartech.forge.http.functionality.HttpFunctionality;
import com.bolyartech.forge.misc.JsonFunctionality;

import java.io.IOException;

/**
 * Created by ogre on 2015-10-11
 */


/**
 * Rest exchange
 * <p/>
 * Only single execution is allowed.
 *
 * @param <T> Type of the returned object
 */
public interface RestExchange<T> {
    /**
     * Executes the exchange using the provided HttpFunctionality
     *
     * @param mHttpFunc HttpFunctionality to be used to execute the exchanges
     * @return Object of type <code>T</code>
     * @throws IOException
     * @throws JsonFunctionality.JsonParseException
     */
    T execute(HttpFunctionality mHttpFunc) throws IOException, JsonFunctionality.JsonParseException;

    /**
     * @return tag object
     */
    Object getTag();

    /**
     * @return if already executed <code>true</code>, <code>false</code> otherwise
     */
    boolean isExecuted();

    /**
     * If exchange is cancelled before it is executed {@link #execute(HttpFunctionality) will return <code>null</code>}.
     * If it is cancelled while waiting for HTTP request to complete it will return <code>null</code> no
     * matter if it was successful or not.
     */
    void cancel();

    /**
     * @return <code>true</code> if cancelled, <code>false</code> otherwise
     */
    boolean isCancelled();
}
