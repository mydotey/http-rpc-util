package org.mydotey.rpc.client.http.apache;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.mydotey.codec.Codec;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface HttpRequestFactory {

    static final List<String> ENTITY_ENCLOSING_REQUEST_METHODS = Collections
            .unmodifiableList(Arrays.asList(HttpPost.METHOD_NAME, HttpPut.METHOD_NAME));

    static HttpEntityEnclosingRequestBase createRequest(String uri, String method, Object data, Codec codec) {
        Objects.requireNonNull(method, "method is null");
        Objects.requireNonNull(data, "data is null");
        Objects.requireNonNull(codec, "codec is null");

        method = method.trim().toUpperCase();
        if (!ENTITY_ENCLOSING_REQUEST_METHODS.contains(method))
            throw new IllegalArgumentException("not supported: " + method);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        codec.encode(os, data);
        ByteArrayEntity entity = new ByteArrayEntity(os.toByteArray());

        HttpEntityEnclosingRequestBase request = (HttpEntityEnclosingRequestBase) createRequest(uri, method);
        request.setEntity(entity);
        request.addHeader("Content-Type", codec.getMimeType());
        request.addHeader("Accept", codec.getMimeType());

        return request;
    }

    static HttpUriRequest createRequest(String uri, String method) {
        Objects.requireNonNull(uri, "uri is null");
        Objects.requireNonNull(method, "method is null");

        uri = uri.trim();
        if (uri.isEmpty())
            throw new IllegalArgumentException("uri is empty");

        method = method.trim().toUpperCase();
        if (method.isEmpty())
            throw new IllegalArgumentException("method is empty");

        HttpRequestBase request = null;
        switch (method) {
            case HttpGet.METHOD_NAME:
                request = new HttpGet(uri);
                break;
            case HttpPost.METHOD_NAME:
                request = new HttpPost(uri);
                break;
            case HttpPut.METHOD_NAME:
                request = new HttpPut(uri);
                break;
            case HttpDelete.METHOD_NAME:
                request = new HttpDelete(uri);
                break;
            case HttpHead.METHOD_NAME:
                request = new HttpHead(uri);
                break;
            case HttpOptions.METHOD_NAME:
                request = new HttpOptions(uri);
                break;
            default:
                throw new IllegalArgumentException("not supported: " + method);
        }

        return request;
    }

    static void gzipRequest(HttpEntityEnclosingRequest request) {
        Objects.requireNonNull(request, "request is null");
        HttpEntity entity = request.getEntity();
        Objects.requireNonNull(entity, "entity is null");
        if (entity instanceof GzipCompressingEntity)
            return;

        GzipCompressingEntity gzippedEntity = new GzipCompressingEntity(entity);
        request.setEntity(gzippedEntity);
        request.addHeader(gzippedEntity.getContentEncoding());
    }

}
