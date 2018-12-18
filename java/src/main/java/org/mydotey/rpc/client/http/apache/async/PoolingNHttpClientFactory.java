package org.mydotey.rpc.client.http.apache.async;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.conn.NHttpClientConnectionManager;
import org.apache.http.protocol.RequestContent;
import org.mydotey.rpc.client.http.apache.AlwaysRedirectStrategy;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface PoolingNHttpClientFactory {

    static CloseableHttpAsyncClient create(RequestConfig config, NHttpClientConnectionManager connectionManager) {
        HttpAsyncClientBuilder builder = HttpAsyncClientBuilder.create();
        builder.useSystemProperties().setRedirectStrategy(AlwaysRedirectStrategy.DEFAULT)
                .addInterceptorLast(new RequestContent(true));
        if (config != null)
            builder.setDefaultRequestConfig(config);
        if (connectionManager != null)
            builder.setConnectionManager(connectionManager);
        return builder.build();
    }

}