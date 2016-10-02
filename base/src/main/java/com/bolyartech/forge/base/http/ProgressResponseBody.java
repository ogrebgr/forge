package com.bolyartech.forge.base.http;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;


/**
 * Response body used to hold progress of a download
 */
public class ProgressResponseBody extends ResponseBody {

    private final ResponseBody responseBody;
    private final Listener progressListener;
    private BufferedSource bufferedSource;


    /**
     * Creates new ProgressResponseBody
     * @param responseBody Normal response body
     * @param progressListener Progress listener
     */
    @SuppressWarnings("unused")
    public ProgressResponseBody(ResponseBody responseBody, Listener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }


    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }


    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }


    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }


    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;


            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                return bytesRead;
            }

        };
    }


    @SuppressWarnings("unused")
    public interface Listener {
        /**
         * Updates the interested party with the progress of the download
         * @param bytesRead Bytes downloaded so far
         * @param contentLength Whole content length, i.e. size of the downloaded file
         * @param done true if download is completed, false otherwise
         */
        @SuppressWarnings("unused")
        void update(long bytesRead, long contentLength, boolean done);
    }
}