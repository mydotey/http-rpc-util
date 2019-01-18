package org.mydotey.rpc.client.http.apache;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Supplier;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.mydotey.scf.ConfigurationManager;
import org.mydotey.scf.Property;
import org.mydotey.scf.PropertyChangeEvent;
import org.mydotey.scf.PropertyConfig;
import org.mydotey.scf.facade.ConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public abstract class AbstractDynamicPoolingHttpClientProvider<T extends Closeable> implements Supplier<T>, Closeable {

    private static final Logger _logger = LoggerFactory.getLogger(AbstractDynamicPoolingHttpClientProvider.class);

    private static final ExecutorService _clientDestroyExecutor;

    static {
        _clientDestroyExecutor = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "dynamic-pooling-httpclient-provider-client-destroy-thread");
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    private String _clientId;
    private DynamicPoolingHttpClientProviderConfig _defaultConfig;
    private Property<String, DynamicPoolingHttpClientProviderConfig> _config;

    private volatile T _client;

    public AbstractDynamicPoolingHttpClientProvider(String clientId, ConfigurationManager configurationManager) {
        Objects.requireNonNull(clientId, "clientId is null");
        _clientId = clientId.trim();
        if (_clientId.isEmpty())
            throw new IllegalArgumentException("clientId is empty");

        Objects.requireNonNull(configurationManager, "configurationManager is null");

        _defaultConfig = newDefaultConfig();
        PropertyConfig<String, DynamicPoolingHttpClientProviderConfig> propertyConfig = ConfigurationProperties
                .<String, DynamicPoolingHttpClientProviderConfig> newConfigBuilder()
                .setKey(_clientId + ".default-request-config")
                .setValueType(DynamicPoolingHttpClientProviderConfig.class).setDefaultValue(_defaultConfig)
                .setValueFilter(this::filterConfig).build();
        _config = configurationManager.getProperty(propertyConfig);
        _config.addChangeListener(this::updateClient);

        updateClient(null);
    }

    @Override
    public void close() throws IOException {
        T client = _client;
        if (client != null) {
            _client = null;
            client.close();
        }
    }

    public String getClientId() {
        return _clientId;
    }

    public DynamicPoolingHttpClientProviderConfig getConfig() {
        return _config.getValue();
    }

    public T get() {
        return _client;
    }

    protected DynamicPoolingHttpClientProviderConfig newDefaultConfig() {
        DynamicPoolingHttpClientProviderConfig defaultConfig = new DynamicPoolingHttpClientProviderConfig();
        defaultConfig.setAuthenticationEnabled(RequestConfig.DEFAULT.isAuthenticationEnabled());
        defaultConfig.setCircularRedirectsAllowed(RequestConfig.DEFAULT.isCircularRedirectsAllowed());
        defaultConfig.setCleanCheckInterval(5 * 1000);
        defaultConfig.setConnectionIdleTime(10 * 1000);
        defaultConfig.setConnectionRequestTimeout(1 * 1000);
        defaultConfig.setConnectionTtl(5 * 60 * 1000);
        defaultConfig.setConnectTimeout(1 * 1000);
        defaultConfig.setContentCompressionEnabled(RequestConfig.DEFAULT.isContentCompressionEnabled());
        defaultConfig.setDestroyDelayTime(60 * 1000);
        defaultConfig.setExpectContinueEnabled(RequestConfig.DEFAULT.isExpectContinueEnabled());
        defaultConfig.setInactivityTimeBeforeValidate(5 * 1000);
        defaultConfig.setIoThreadCount(1);
        defaultConfig.setMaxConnectionsPerRoute(10);
        defaultConfig.setMaxRedirects(RequestConfig.DEFAULT.getMaxRedirects());
        defaultConfig.setMaxTotalConections(100);
        defaultConfig.setRedirectsEnabled(RequestConfig.DEFAULT.isRedirectsEnabled());
        defaultConfig.setRelativeRedirectsAllowed(RequestConfig.DEFAULT.isRelativeRedirectsAllowed());
        defaultConfig.setRetryIOExceptions(Arrays.asList(NoHttpResponseException.class.getName()));
        defaultConfig.setRetryTimes(1);
        defaultConfig.setSocketTimeout(10 * 1000);
        return defaultConfig;
    }

    protected DynamicPoolingHttpClientProviderConfig filterConfig(DynamicPoolingHttpClientProviderConfig config) {
        if (config.getAuthenticationEnabled() == null)
            config.setAuthenticationEnabled(_defaultConfig.getAuthenticationEnabled());
        if (config.getCircularRedirectsAllowed() == null)
            config.setCircularRedirectsAllowed(_defaultConfig.getCircularRedirectsAllowed());
        if (config.getContentCompressionEnabled() == null)
            config.setContentCompressionEnabled(_defaultConfig.getContentCompressionEnabled());
        if (config.getExpectContinueEnabled() == null)
            config.setExpectContinueEnabled(_defaultConfig.getExpectContinueEnabled());
        if (config.getRedirectsEnabled() == null)
            config.setRedirectsEnabled(_defaultConfig.getRedirectsEnabled());
        if (config.getRelativeRedirectsAllowed() == null)
            config.setRelativeRedirectsAllowed(_defaultConfig.getRelativeRedirectsAllowed());
        if (config.getRetryIOExceptions() == null || config.getRetryIOExceptions().isEmpty())
            config.setRetryIOExceptions(_defaultConfig.getRetryIOExceptions());
        if (config.getRetryTimes() == null || config.getRetryTimes() < 0)
            config.setRetryTimes(_defaultConfig.getRetryTimes());
        if (config.getCleanCheckInterval() == null || config.getCleanCheckInterval() < 0)
            config.setCleanCheckInterval(_defaultConfig.getCleanCheckInterval());
        if (config.getConnectionIdleTime() == null || config.getConnectionIdleTime() <= 0)
            config.setConnectionIdleTime(_defaultConfig.getConnectionIdleTime());
        if (config.getConnectionRequestTimeout() == null || config.getConnectionRequestTimeout() <= 0)
            config.setConnectionRequestTimeout(_defaultConfig.getConnectionRequestTimeout());
        if (config.getConnectionTtl() == null || config.getConnectionTtl() <= 0)
            config.setConnectionTtl(_defaultConfig.getConnectionTtl());
        if (config.getConnectTimeout() == null || config.getConnectTimeout() <= 0)
            config.setConnectTimeout(_defaultConfig.getConnectionTtl());
        if (config.getDestroyDelayTime() == null || config.getDestroyDelayTime() < 0)
            config.setDestroyDelayTime(_defaultConfig.getDestroyDelayTime());
        if (config.getInactivityTimeBeforeValidate() == null || config.getInactivityTimeBeforeValidate() <= 0)
            config.setInactivityTimeBeforeValidate(_defaultConfig.getInactivityTimeBeforeValidate());
        if (config.getIoThreadCount() == null || config.getIoThreadCount() <= 0)
            config.setIoThreadCount(_defaultConfig.getIoThreadCount());
        if (config.getMaxConnectionsPerRoute() == null || config.getMaxConnectionsPerRoute() <= 0)
            config.setMaxConnectionsPerRoute(_defaultConfig.getMaxConnectionsPerRoute());
        if (config.getMaxRedirects() == null || config.getMaxRedirects() <= 0)
            config.setMaxRedirects(_defaultConfig.getMaxRedirects());
        if (config.getMaxTotalConections() == null || config.getMaxTotalConections() <= 0)
            config.setMaxTotalConections(_defaultConfig.getMaxTotalConections());
        if (config.getSocketTimeout() == null || config.getSocketTimeout() <= 0)
            config.setSocketTimeout(_defaultConfig.getSocketTimeout());

        return config;
    }

    protected abstract T createClient();

    private void updateClient(PropertyChangeEvent<String, DynamicPoolingHttpClientProviderConfig> e) {
        T client = _client;
        _client = createClient();
        _logger.info("updated http client: {}", _clientId);

        if (client == null)
            return;

        if (e != null)
            _logger.info("config changed from {} to {}", e.getOldValue(), e.getNewValue());

        _clientDestroyExecutor.submit(() -> {
            try {
                Thread.sleep(_config.getValue().getDestroyDelayTime());
            } catch (Exception ex) {
            }

            try {
                client.close();
                _logger.info("Old httpclient is distroyed: {}", _clientId);
            } catch (Exception ex) {
                _logger.error("Close old httpclient failed: " + _clientId, ex);
            }
        });
    }

}
