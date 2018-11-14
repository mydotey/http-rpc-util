package org.mydotey.rpc.client.http.apache;

import java.io.IOException;
import java.util.Objects;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.mydotey.codec.Codec;
import org.mydotey.codec.json.JacksonJsonCodec;
import org.mydotey.rpc.client.http.apache.async.DynamicPoolingNHttpClientProvider;
import org.mydotey.rpc.client.http.apache.sync.DynamicPoolingHttpClientProvider;
import org.mydotey.scf.ConfigurationManager;
import org.mydotey.scf.Property;
import org.mydotey.scf.facade.StringProperties;

/**
 * @author koqizhao
 *
 * Nov 8, 2018
 */
public class TestApacheHttpRpcClient extends ApacheHttpRpcClient {

    private String _serviceId;
    private StringProperties _stringProperties;

    private String _clientId;
    private Property<String, String> _serviceUrl;
    private DynamicPoolingHttpClientProvider _httpClientProvider;
    private DynamicPoolingNHttpClientProvider _httpAsyncClientProvider;

    public TestApacheHttpRpcClient(String serviceId, ConfigurationManager configurationManager) {
        Objects.requireNonNull(serviceId, "serviceId is null");
        _serviceId = serviceId.trim();
        if (_serviceId.isEmpty())
            throw new IllegalArgumentException("serviceId is empty");

        Objects.requireNonNull(configurationManager, "configurationManager is null");
        _stringProperties = new StringProperties(configurationManager);

        _serviceUrl = _stringProperties.getStringProperty(_serviceId + ".service.url");
        _clientId = _serviceId + ".client";
    }

    @Override
    public void close() throws IOException {
        if (_httpClientProvider != null)
            _httpClientProvider.close();

        if (_httpAsyncClientProvider != null)
            _httpAsyncClientProvider.close();
    }

    @Override
    protected CloseableHttpClient getHttpClient() {
        if (_httpClientProvider != null)
            return _httpClientProvider.get();

        synchronized (this) {
            if (_httpClientProvider == null)
                _httpClientProvider = new DynamicPoolingHttpClientProvider(_clientId, _stringProperties.getManager());

            return _httpClientProvider.get();
        }
    }

    @Override
    protected CloseableHttpAsyncClient getHttpAsyncClient() {
        if (_httpAsyncClientProvider != null)
            return _httpAsyncClientProvider.get();

        synchronized (this) {
            if (_httpAsyncClientProvider == null)
                _httpAsyncClientProvider = new DynamicPoolingNHttpClientProvider(_clientId,
                        _stringProperties.getManager());

            return _httpAsyncClientProvider.get();
        }
    }

    @Override
    protected <Req> HttpUriRequest toHttpUriRequest(String procedure, Req request) {
        String url = getServiceUrl();
        if (!url.endsWith("/"))
            url += "/";
        url += procedure;
        return HttpRequestFactory.createRequest(url, HttpPost.METHOD_NAME, request, getCodec());
    }

    @Override
    protected Codec getCodec() {
        return JacksonJsonCodec.DEFAULT;
    }

    public String getServiceId() {
        return _serviceId;
    }

    protected String getClientId() {
        return _clientId;
    }

    protected String getServiceUrl() {
        return _serviceUrl.getValue();
    }

    protected StringProperties getStringProperties() {
        return _stringProperties;
    }

}
