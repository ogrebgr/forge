package com.bolyartech.forge.http.misc;

import com.bolyartech.forge.http.request.ProgressListener;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import forge.apache.http.HttpEntity;
import forge.apache.http.entity.HttpEntityWrapper;


/**
 * Created by ogre on 2015-11-08 11:03
 */
public class ProgressHttpEntityWrapper extends HttpEntityWrapper {

    private final ProgressListener mProgressListener;


    public ProgressHttpEntityWrapper(final HttpEntity entity, final ProgressListener progressListener) {
        super(entity);
        mProgressListener = progressListener;
    }


    @Override
    public void writeTo(final OutputStream out) throws IOException {
        OutputStream destination;
        if (out instanceof ProgressFilterOutputStream) {
            destination = out;
        } else {
            destination = new ProgressFilterOutputStream(out, mProgressListener, getContentLength());
        }
        this.wrappedEntity.writeTo(destination);
    }
}
