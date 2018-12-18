package org.mydotey.rpc.client.http.apache.sync;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.function.Function;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.mydotey.codec.Codec;
import org.mydotey.codec.CodecException;
import org.mydotey.rpc.client.http.HttpConnectException;
import org.mydotey.rpc.client.http.HttpTimeoutException;
import org.mydotey.rpc.client.http.apache.ApacheHttpRequestException;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface HttpRequestExecutors {

    static <T> T execute(CloseableHttpClient client, HttpUriRequest request, Codec codec, Class<T> clazz) {
        return execute(client, request, r -> {
            try (InputStream is = r.getEntity().getContent()) {
                return codec.decode(is, clazz);
            } catch (IOException ex) {
                throw new CodecException(ex);
            }
        });
    }

    static <T> T execute(CloseableHttpClient client, HttpUriRequest request, Function<HttpResponse, T> dataHandler) {
        try (CloseableHttpResponse response = client.execute(request);) {
            StatusLine statusLine = response.getStatusLine();
            if (statusLine == null || statusLine.getStatusCode() >= 300)
                throw new ApacheHttpRequestException(response);

            return dataHandler.apply(response);
        } catch (SocketTimeoutException e) {
            throw new HttpTimeoutException(e);
        } catch (IOException e) {
            throw new HttpConnectException(e);
        }
    }

}
