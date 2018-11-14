package org.mydotey.rpc.client.http.apache.async;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.mydotey.codec.Codec;
import org.mydotey.rpc.client.http.apache.HttpRequestException;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class HttpRequestAsyncExecutors {

    public static <T> CompletableFuture<T> executeAsync(CloseableHttpAsyncClient client, HttpUriRequest request,
            Codec codec, Class<T> clazz) {
        return executeAsync(ForkJoinPool.commonPool(), client, request, codec, clazz);
    }

    public static <T> CompletableFuture<T> executeAsync(Executor executor, CloseableHttpAsyncClient client,
            HttpUriRequest request, Codec codec, Class<T> clazz) {
        Objects.requireNonNull(executor, "executor is null");
        Objects.requireNonNull(codec, "codec is null");
        Objects.requireNonNull(clazz, "clazz is null");

        CompletableFuture<HttpResponse> future = executeAsync(client, request);
        return future.thenApplyAsync(r -> {
            try {
                return codec.decode(r.getEntity().getContent(), clazz);
            } catch (UnsupportedOperationException | IOException e) {
                throw new HttpRequestException(e);
            }
        }, executor);
    }

    public static CompletableFuture<HttpResponse> executeAsync(CloseableHttpAsyncClient client,
            HttpUriRequest request) {
        Objects.requireNonNull(client, "client is null");
        Objects.requireNonNull(request, "request is null");

        CompletableFuture<HttpResponse> future = new CompletableFuture<>();
        client.execute(request, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse result) {
                future.complete(result);
            }

            @Override
            public void failed(Exception ex) {
                future.completeExceptionally(ex);
            }

            @Override
            public void cancelled() {
                Exception ex = new CancellationException("Request has been cancelled.");
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    private HttpRequestAsyncExecutors() {

    }

}
