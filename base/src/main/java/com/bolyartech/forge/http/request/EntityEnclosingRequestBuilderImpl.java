package com.bolyartech.forge.http.request;

import com.bolyartech.forge.http.misc.ProgressHttpEntityWrapper;
import com.bolyartech.forge.misc.ForUnitTestsOnly;
import com.google.common.base.Charsets;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import forge.apache.http.Header;
import forge.apache.http.NameValuePair;
import forge.apache.http.client.entity.UrlEncodedFormEntity;
import forge.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import forge.apache.http.client.utils.URIBuilder;
import forge.apache.http.entity.ContentType;
import forge.apache.http.entity.mime.MultipartEntityBuilder;
import forge.apache.http.entity.mime.content.FileBody;
import forge.apache.http.entity.mime.content.StringBody;
import forge.apache.http.message.BasicNameValuePair;


/**
 * Created by ogre on 2015-11-01
 */
abstract public class EntityEnclosingRequestBuilderImpl<T extends HttpEntityEnclosingRequestBase> extends BaseRequestBuilder implements EntityEnclosingRequestBuilder {
    private static final Charset DEFAULT_CHARSET = Charsets.UTF_8;

    private final ArrayList<NameValuePair> mPostParams = new ArrayList<>();
    private final Map<String, File> mFilesToUpload = new HashMap<>();
    private final Charset mCharset;

    abstract protected T createRequest(URI uri);

    public EntityEnclosingRequestBuilderImpl(String url) {
        this(url, DEFAULT_CHARSET);
    }


    @SuppressWarnings("SameParameterValue")
    public EntityEnclosingRequestBuilderImpl(String url, Charset charset) {
        super(url);

        if (charset != null) {
            mCharset = charset;
        } else {
            throw new NullPointerException("Parameter 'charset' cannot be null");
        }

    }


    public T build() {
        T ret;

        String protocol = getProtocol();
        if (protocol != null && !protocol.equals("") && getDomain() != null
                && !getDomain().equals("") && getPath() != null && !getPath().equals("")) {
            URI uri;
            try {
                URIBuilder ub = new URIBuilder();
                ub.setScheme(protocol).setHost(getDomain()).setPort(getPort()).setPath(getPath());
                if (getGetParams().size() > 0) {
                    ub.setParameters(getGetParams());
                }
                uri = ub.build();
            } catch (URISyntaxException e) {
                throw new IllegalStateException("Error creating URI.", e);
            }

            ret = createRequest(uri);
            if (!isHeaderPresent("Accept-Charset")) {
                ret.addHeader("Accept-Charset", getCharset() + ",*");
            }

            if (getHeaders().size() > 0) {
                for(Header h : getHeaders()) {
                    ret.addHeader(h);
                }
            }

            if (mFilesToUpload.size() == 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(mPostParams, mCharset);
                ret.setEntity(entity);
            } else {
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

                for (String key : mFilesToUpload.keySet()) {
                    entityBuilder.addPart(key, new FileBody(mFilesToUpload.get(key)));
                }

                ContentType ct = ContentType.TEXT_PLAIN;
                for (NameValuePair p : mPostParams) {
                    entityBuilder.addPart(p.getName(), new StringBody(p.getValue(), ct));
                }

                if (getProgressListener() != null) {
                    ret.setEntity(new ProgressHttpEntityWrapper(entityBuilder.build(), getProgressListener()));
                } else {
                    ret.setEntity(entityBuilder.build());
                }
            }

        } else {
            throw new IllegalStateException("Some of required fields (protocol, domain, path) are empty.");
        }

        return ret;
    }


    public void postParameter(String paramName, String value) {
        if (isPostParameterMissing(paramName) && !mFilesToUpload.containsKey(paramName)) {
            NameValuePair tmp = new BasicNameValuePair(paramName, value);
            mPostParams.add(tmp);
        } else {
            throw new IllegalArgumentException("There is already POST parameter named " + paramName);
        }
    }


    @Override
    public boolean isPostParameterPresent(String paramName) {
        return !isPostParameterMissing(paramName);
    }


    /**
     * Checks if POST parameter is NOT present
     *
     * @param paramName Name of the parameter
     * @return true if NOT present, false otherwise
     */
    public boolean isPostParameterMissing(String paramName) {
        boolean ret = false;

        for (NameValuePair pair : mPostParams) {
            if (pair.getName().equals(paramName)) {
                ret = true;
                break;
            }
        }

        return !ret;
    }


    public void fileToUpload(String paramName, File file) {
        if (!mFilesToUpload.containsKey(paramName) && isPostParameterMissing(paramName)) {
            if (isPostParameterMissing(paramName)) {
                if (file.exists()) {
                    mFilesToUpload.put(paramName, file);
                } else {
                    throw new IllegalArgumentException("File does not exist "
                            + file.getAbsolutePath());
                }
            } else {
                throw new IllegalArgumentException("There is already POST parameter named "
                        + paramName);
            }
        } else {
            throw new IllegalArgumentException("There is already added file for upload for param name '"
                    + paramName + "'");
        }
    }


    @ForUnitTestsOnly
    ArrayList<NameValuePair> getPostParams() {
        return mPostParams;
    }


    @ForUnitTestsOnly
    Map<String, File> getFilesToUpload() {
        return mFilesToUpload;
    }
}
