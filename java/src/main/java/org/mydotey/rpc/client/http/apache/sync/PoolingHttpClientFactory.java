package org.mydotey.rpc.client.http.apache.sync;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.RequestContent;
import org.mydotey.rpc.client.http.apache.AlwaysRedirectStrategy;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class PoolingHttpClientFactory {

    private PoolingHttpClientFactory() {
        super();
    }

    public static CloseableHttpClient create(RequestConfig config, HttpRequestRetryHandler retryHandler,
            HttpClientConnectionManager connectionManager) {
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.useSystemProperties().setRedirectStrategy(AlwaysRedirectStrategy.DEFAULT)
                .addInterceptorLast(new RequestContent(true));
        if (config != null)
            builder.setDefaultRequestConfig(config);
        if (retryHandler != null)
            builder.setRetryHandler(retryHandler);
        if (connectionManager != null)
            builder.setConnectionManager(connectionManager);
        return builder.build();
    }

}