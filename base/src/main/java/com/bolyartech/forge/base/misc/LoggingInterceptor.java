package com.bolyartech.forge.base.misc;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Logging interceptor to be used for debugging
 */
@SuppressWarnings("unused")
public class LoggingInterceptor implements Interceptor {
    @SuppressWarnings("unused")
    private final org.slf4j.Logger mLogger = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("unused")
    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        mLogger.trace(String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        try {
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            mLogger.trace(String.format(Locale.US, "Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;

        } catch (Exception e) {
            mLogger.debug("Problem executing HTTP request {}", e);
            throw new RuntimeException("Problem executing HTTP request");
        }
    }
}