package org.mydotey.rpc.client.http.apache;

import java.util.concurrent.CompletableFuture;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.mydotey.codec.Codec;
import org.mydotey.rpc.client.RpcClient;
import org.mydotey.rpc.client.http.apache.async.HttpRequestAsyncExecutors;
import org.mydotey.rpc.client.http.apache.sync.HttpRequestExecutors;

/**
 * @author koqizhao
 *
 * Nov 6, 2018
 */
public abstract class ApacheHttpRpcClient implements RpcClient {

    @Override
    public <Req, Res> Res invoke(String procedure, Req request, Class<Res> clazz) {
        CloseableHttpClient client = getHttpClient();
        HttpUriRequest httpUriRequest = toHttpUriRequest(procedure, request);
        Codec codec = getCodec();
        return HttpRequestExecutors.execute(client, httpUriRequest, codec, clazz);
    }

    @Override
    public <Req, Res> CompletableFuture<Res> invokeAsync(String procedure, Req request, Class<Res> clazz) {
        CloseableHttpAsyncClient client = getHttpAsyncClient();
        HttpUriRequest httpUriRequest = toHttpUriRequest(procedure, request);
        Codec codec = getCodec();
        return HttpRequestAsyncExecutors.executeAsync(client, httpUriRequest, codec, clazz);
    }

    protected abstract CloseableHttpClient getHttpClient();

    protected abstract CloseableHttpAsyncClient getHttpAsyncClient();

    protected abstract <Req> HttpUriRequest toHttpUriRequest(String procedure, Req request);

    protected abstract Codec getCodec();

}
