package com.bolyartech.forge.http.misc;

import com.bolyartech.forge.http.request.ProgressListener;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Created by ogre on 2015-11-09 08:38
 */
class ProgressFilterOutputStream extends FilterOutputStream {

    private final ProgressListener mProgressListener;
    private long mTransferred;
    private long mTotalBytes;


    ProgressFilterOutputStream(final OutputStream out, final ProgressListener progressListener, final long totalBytes) {
        super(out);

        mProgressListener = progressListener;
        this.mTransferred = 0;
        this.mTotalBytes = totalBytes;
    }


    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        //super.write(byte b[], int off, int len) calls write(int b)
        out.write(b, off, len);
        this.mTransferred += len;
        mProgressListener.onProgress(getCurrentProgress());
    }


    @Override
    public void write(final int b) throws IOException {
        out.write(b);
        this.mTransferred++;
        mProgressListener.onProgress(getCurrentProgress());
    }


    private float getCurrentProgress() {
        return ((float) this.mTransferred / this.mTotalBytes) * 100;
    }

}