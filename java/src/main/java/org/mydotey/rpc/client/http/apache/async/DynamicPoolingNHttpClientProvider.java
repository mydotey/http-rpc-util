package org.mydotey.rpc.client.http.apache.async;

import java.io.IOException;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.mydotey.rpc.client.http.apache.AbstractDynamicPoolingHttpClientProvider;
import org.mydotey.rpc.client.http.apache.DynamicPoolingHttpClientProviderConfig;
import org.mydotey.scf.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DynamicPoolingNHttpClientProvider
        extends AbstractDynamicPoolingHttpClientProvider<CloseableHttpAsyncClient> {

    private static final Logger _logger = LoggerFactory.getLogger(DynamicPoolingNHttpClientProvider.class);

    public DynamicPoolingNHttpClientProvider(String clientId, ConfigurationManager configurationManager) {
        super(clientId, configurationManager);
    }

    @Override
    protected CloseableHttpAsyncClient createClient() {
        RequestConfig requestConfig = DynamicPoolingHttpClientProviderConfig.toRequestConfig(getConfig());
        AutoCleanedPoolingNHttpClientConnectionManager connectionManager = new AutoCleanedPoolingNHttpClientConnectionManager(
                getConfig().getIoThreadCount(), getConfig().getConnectionTtl(), getConfig().getConnectionIdleTime(),
                getConfig().getCleanCheckInterval());
        connectionManager.setDefaultMaxPerRoute(getConfig().getMaxConnectionsPerRoute());
        connectionManager.setMaxTotal(getConfig().getMaxTotalConections());
        CloseableHttpAsyncClient client = PoolingNHttpClientFactory.create(requestConfig, connectionManager);
        try {
            client.start();
            return client;
        } catch (Throwable ex) {
            try {
                client.close();
            } catch (IOException e) {
                _logger.warn("create async client failed", e);
            }

            throw ex;
        }
    }

}
