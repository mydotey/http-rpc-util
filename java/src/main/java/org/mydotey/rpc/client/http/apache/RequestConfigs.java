package org.mydotey.rpc.client.http.apache;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.Configurable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class RequestConfigs {

    private static final String DEFAULT_CONFIG_FIELD = "defaultConfig";
    private static final ConcurrentHashMap<Class<?>, AtomicReference<Field>> _clazzDefaultConfigFieldMap = new ConcurrentHashMap<>();

    private static final Logger _logger = LoggerFactory.getLogger(RequestConfigs.class);

    private RequestConfigs() {

    }

    public static RequestConfig createCascadedRequestConfig(RequestConfig genericConfig, RequestConfig concreteConfig) {
        if (genericConfig == null && concreteConfig == null)
            return null;

        if (genericConfig == null)
            return RequestConfig.copy(concreteConfig).build();

        if (concreteConfig == null)
            return RequestConfig.copy(genericConfig).build();

        RequestConfig.Builder builder = RequestConfig.copy(concreteConfig);

        if (concreteConfig.getConnectionRequestTimeout() <= 0)
            builder.setConnectionRequestTimeout(genericConfig.getConnectionRequestTimeout());

        if (concreteConfig.getConnectTimeout() <= 0)
            builder.setConnectTimeout(genericConfig.getConnectTimeout());

        if (concreteConfig.getSocketTimeout() <= 0)
            builder.setSocketTimeout(genericConfig.getSocketTimeout());

        if (concreteConfig.getCookieSpec() == null)
            builder.setCookieSpec(genericConfig.getCookieSpec());

        if (concreteConfig.getLocalAddress() == null)
            builder.setLocalAddress(genericConfig.getLocalAddress());

        if (concreteConfig.getProxy() == null)
            builder.setProxy(genericConfig.getProxy());

        if (concreteConfig.getMaxRedirects() <= 0)
            builder.setMaxRedirects(genericConfig.getMaxRedirects());

        if (concreteConfig.getTargetPreferredAuthSchemes() == null)
            builder.setTargetPreferredAuthSchemes(genericConfig.getTargetPreferredAuthSchemes());

        if (concreteConfig.getProxyPreferredAuthSchemes() == null)
            builder.setProxyPreferredAuthSchemes(genericConfig.getProxyPreferredAuthSchemes());

        return builder.build();
    }

    public static RequestConfig getRequestConfig(Object client) {
        if (client == null)
            return null;

        if (client instanceof Configurable)
            return ((Configurable) client).getConfig();

        return getRequestConfigFromField(client);
    }

    private static RequestConfig getRequestConfigFromField(Object client) {
        if (client == null)
            return null;

        final Class<?> clazz = client.getClass();
        AtomicReference<Field> defaultConfigFieldReference = _clazzDefaultConfigFieldMap.computeIfAbsent(clazz, k -> {
            Field defaultConfigField = null;
            try {
                defaultConfigField = clazz.getDeclaredField(DEFAULT_CONFIG_FIELD);
                if (defaultConfigField != null && defaultConfigField.getType() != RequestConfig.class) {
                    defaultConfigField = null;
                    _logger.info("Got a field defaultConfig from clazz {}, but not the type RequestConfig.", clazz);
                } else {
                    defaultConfigField.setAccessible(true);
                    _logger.info("Got a field defaultConfig of type RequestConfig from clazz {}.", clazz);
                }
            } catch (Throwable ex) {
                _logger.info("Cannot get a field defaultConfig from clazz " + clazz + ".", ex);
            }

            return new AtomicReference<Field>(defaultConfigField);
        });

        Field defaultConfigField = defaultConfigFieldReference.get();
        if (defaultConfigField == null)
            return null;

        try {
            return (RequestConfig) defaultConfigField.get(client);
        } catch (Exception ex) {
            _logger.info("Cannot get value from defaultConfig field of clazz " + clazz, ex);
            return null;
        }
    }

}
