package com.bolyartech.forge.base.http;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;


public class CountingFileRequestBody extends RequestBody {

    private static final int SEGMENT_SIZE = 2048;

    private final File mFile;
    private final ProgressListener mListener;
    private final MediaType mMediaType;


    @SuppressWarnings("unused")
    public CountingFileRequestBody(File file, MediaType mediaType, ProgressListener listener) {
        mFile = file;
        mMediaType = mediaType;
        mListener = listener;
    }


    @Override
    public long contentLength() {
        return mFile.length();
    }


    @Override
    public MediaType contentType() {
        return mMediaType;
    }


    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(mFile);
            long transferred = 0;
            long read;

            while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
                transferred += read;
                sink.flush();
                mListener.transferredSoFar(transferred);

            }
        } finally {
            Util.closeQuietly(source);
        }
    }


    @SuppressWarnings("unused")
    public interface ProgressListener {
        void transferredSoFar(long num);
    }
}