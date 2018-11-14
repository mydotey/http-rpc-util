package org.mydotey.rpc.client.http.apache.sync;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.mydotey.rpc.client.http.apache.AbstractDynamicPoolingHttpClientProvider;
import org.mydotey.rpc.client.http.apache.DynamicPoolingHttpClientProviderConfig;
import org.mydotey.scf.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DynamicPoolingHttpClientProvider extends AbstractDynamicPoolingHttpClientProvider<CloseableHttpClient> {

    private static final Logger _logger = LoggerFactory.getLogger(DynamicPoolingHttpClientProvider.class);

    public DynamicPoolingHttpClientProvider(String clientId, ConfigurationManager configurationManager) {
        super(clientId, configurationManager);
    }

    @Override
    protected CloseableHttpClient createClient() {
        RequestConfig requestConfig = DynamicPoolingHttpClientProviderConfig.toRequestConfig(getConfig());
        IOExceptionRetryHelper ioExceptionRetryHelper = new IOExceptionRetryHelper(getConfig().getRetryTimes());
        for (String exception : getConfig().getRetryIOExceptions()) {
            try {
                Class<?> clazz = Class.forName(exception);
                ioExceptionRetryHelper.addIOExceptionsToBeRetried(clazz);
            } catch (ClassNotFoundException e) {
                _logger.warn("invalid type", e);
            }
        }

        AutoCleanedPoolingHttpClientConnectionManager connectionManager = new AutoCleanedPoolingHttpClientConnectionManager(
                getConfig().getConnectionTtl(), getConfig().getInactivityTimeBeforeValidate(),
                getConfig().getConnectionIdleTime(), getConfig().getCleanCheckInterval());
        connectionManager.setDefaultMaxPerRoute(getConfig().getMaxConnectionsPerRoute());
        connectionManager.setMaxTotal(getConfig().getMaxTotalConections());

        return PoolingHttpClientFactory.create(requestConfig, ioExceptionRetryHelper.retryHandler(), connectionManager);
    }

}
