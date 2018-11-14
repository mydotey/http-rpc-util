package org.mydotey.rpc.client.http.apache;

import java.util.List;
import java.util.Objects;

import org.apache.http.client.config.RequestConfig;

/**
 * @author koqizhao
 *
 * Nov 6, 2018
 */
public class DynamicPoolingHttpClientProviderConfig {

    public static RequestConfig toRequestConfig(DynamicPoolingHttpClientProviderConfig config) {
        Objects.requireNonNull(config, "config is null");
        return RequestConfig.custom().setAuthenticationEnabled(config.getAuthenticationEnabled())
                .setCircularRedirectsAllowed(config.getCircularRedirectsAllowed())
                .setConnectionRequestTimeout(config.getConnectionRequestTimeout())
                .setConnectTimeout(config.getConnectTimeout())
                .setContentCompressionEnabled(config.getContentCompressionEnabled())
                .setExpectContinueEnabled(config.getExpectContinueEnabled()).setMaxRedirects(config.getMaxRedirects())
                .setRedirectsEnabled(config.getRedirectsEnabled())
                .setRelativeRedirectsAllowed(config.getRelativeRedirectsAllowed())
                .setSocketTimeout(config.getSocketTimeout()).build();
    }

    private Boolean expectContinueEnabled;
    private Boolean redirectsEnabled;
    private Boolean relativeRedirectsAllowed;
    private Boolean circularRedirectsAllowed;
    private Integer maxRedirects;
    private Boolean authenticationEnabled;
    private Integer connectionRequestTimeout;
    private Integer connectTimeout;
    private Integer socketTimeout;
    private Boolean contentCompressionEnabled;

    private Integer connectionTtl;
    private Integer connectionIdleTime;
    private Integer cleanCheckInterval;
    private Integer inactivityTimeBeforeValidate;
    private Integer maxConnectionsPerRoute;
    private Integer maxTotalConections;

    private Integer retryTimes;
    private List<String> retryIOExceptions;

    private Integer ioThreadCount;

    private Integer destroyDelayTime;

    public Boolean getExpectContinueEnabled() {
        return expectContinueEnabled;
    }

    public void setExpectContinueEnabled(Boolean expectContinueEnabled) {
        this.expectContinueEnabled = expectContinueEnabled;
    }

    public Boolean getRedirectsEnabled() {
        return redirectsEnabled;
    }

    public void setRedirectsEnabled(Boolean redirectsEnabled) {
        this.redirectsEnabled = redirectsEnabled;
    }

    public Boolean getRelativeRedirectsAllowed() {
        return relativeRedirectsAllowed;
    }

    public void setRelativeRedirectsAllowed(Boolean relativeRedirectsAllowed) {
        this.relativeRedirectsAllowed = relativeRedirectsAllowed;
    }

    public Boolean getCircularRedirectsAllowed() {
        return circularRedirectsAllowed;
    }

    public void setCircularRedirectsAllowed(Boolean circularRedirectsAllowed) {
        this.circularRedirectsAllowed = circularRedirectsAllowed;
    }

    public Integer getMaxRedirects() {
        return maxRedirects;
    }

    public void setMaxRedirects(Integer maxRedirects) {
        this.maxRedirects = maxRedirects;
    }

    public Boolean getAuthenticationEnabled() {
        return authenticationEnabled;
    }

    public void setAuthenticationEnabled(Boolean authenticationEnabled) {
        this.authenticationEnabled = authenticationEnabled;
    }

    public Integer getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(Integer connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public Boolean getContentCompressionEnabled() {
        return contentCompressionEnabled;
    }

    public void setContentCompressionEnabled(Boolean contentCompressionEnabled) {
        this.contentCompressionEnabled = contentCompressionEnabled;
    }

    public Integer getConnectionTtl() {
        return connectionTtl;
    }

    public void setConnectionTtl(Integer connectionTtl) {
        this.connectionTtl = connectionTtl;
    }

    public Integer getConnectionIdleTime() {
        return connectionIdleTime;
    }

    public void setConnectionIdleTime(Integer connectionIdleTime) {
        this.connectionIdleTime = connectionIdleTime;
    }

    public Integer getCleanCheckInterval() {
        return cleanCheckInterval;
    }

    public void setCleanCheckInterval(Integer cleanCheckInterval) {
        this.cleanCheckInterval = cleanCheckInterval;
    }

    public Integer getInactivityTimeBeforeValidate() {
        return inactivityTimeBeforeValidate;
    }

    public void setInactivityTimeBeforeValidate(Integer inactivityTimeBeforeValidate) {
        this.inactivityTimeBeforeValidate = inactivityTimeBeforeValidate;
    }

    public Integer getMaxConnectionsPerRoute() {
        return maxConnectionsPerRoute;
    }

    public void setMaxConnectionsPerRoute(Integer maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
    }

    public Integer getMaxTotalConections() {
        return maxTotalConections;
    }

    public void setMaxTotalConections(Integer maxTotalConections) {
        this.maxTotalConections = maxTotalConections;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public List<String> getRetryIOExceptions() {
        return retryIOExceptions;
    }

    public void setRetryIOExceptions(List<String> retryIOExceptions) {
        this.retryIOExceptions = retryIOExceptions;
    }

    public Integer getIoThreadCount() {
        return ioThreadCount;
    }

    public void setIoThreadCount(Integer ioThreadCount) {
        this.ioThreadCount = ioThreadCount;
    }

    public Integer getDestroyDelayTime() {
        return destroyDelayTime;
    }

    public void setDestroyDelayTime(Integer destroyDelayTime) {
        this.destroyDelayTime = destroyDelayTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authenticationEnabled == null) ? 0 : authenticationEnabled.hashCode());
        result = prime * result + ((circularRedirectsAllowed == null) ? 0 : circularRedirectsAllowed.hashCode());
        result = prime * result + ((cleanCheckInterval == null) ? 0 : cleanCheckInterval.hashCode());
        result = prime * result + ((connectTimeout == null) ? 0 : connectTimeout.hashCode());
        result = prime * result + ((connectionIdleTime == null) ? 0 : connectionIdleTime.hashCode());
        result = prime * result + ((connectionRequestTimeout == null) ? 0 : connectionRequestTimeout.hashCode());
        result = prime * result + ((connectionTtl == null) ? 0 : connectionTtl.hashCode());
        result = prime * result + ((contentCompressionEnabled == null) ? 0 : contentCompressionEnabled.hashCode());
        result = prime * result + ((destroyDelayTime == null) ? 0 : destroyDelayTime.hashCode());
        result = prime * result + ((expectContinueEnabled == null) ? 0 : expectContinueEnabled.hashCode());
        result = prime * result
                + ((inactivityTimeBeforeValidate == null) ? 0 : inactivityTimeBeforeValidate.hashCode());
        result = prime * result + ((ioThreadCount == null) ? 0 : ioThreadCount.hashCode());
        result = prime * result + ((maxConnectionsPerRoute == null) ? 0 : maxConnectionsPerRoute.hashCode());
        result = prime * result + ((maxRedirects == null) ? 0 : maxRedirects.hashCode());
        result = prime * result + ((maxTotalConections == null) ? 0 : maxTotalConections.hashCode());
        result = prime * result + ((redirectsEnabled == null) ? 0 : redirectsEnabled.hashCode());
        result = prime * result + ((relativeRedirectsAllowed == null) ? 0 : relativeRedirectsAllowed.hashCode());
        result = prime * result + ((retryIOExceptions == null) ? 0 : retryIOExceptions.hashCode());
        result = prime * result + ((retryTimes == null) ? 0 : retryTimes.hashCode());
        result = prime * result + ((socketTimeout == null) ? 0 : socketTimeout.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DynamicPoolingHttpClientProviderConfig other = (DynamicPoolingHttpClientProviderConfig) obj;
        if (authenticationEnabled == null) {
            if (other.authenticationEnabled != null)
                return false;
        } else if (!authenticationEnabled.equals(other.authenticationEnabled))
            return false;
        if (circularRedirectsAllowed == null) {
            if (other.circularRedirectsAllowed != null)
                return false;
        } else if (!circularRedirectsAllowed.equals(other.circularRedirectsAllowed))
            return false;
        if (cleanCheckInterval == null) {
            if (other.cleanCheckInterval != null)
                return false;
        } else if (!cleanCheckInterval.equals(other.cleanCheckInterval))
            return false;
        if (connectTimeout == null) {
            if (other.connectTimeout != null)
                return false;
        } else if (!connectTimeout.equals(other.connectTimeout))
            return false;
        if (connectionIdleTime == null) {
            if (other.connectionIdleTime != null)
                return false;
        } else if (!connectionIdleTime.equals(other.connectionIdleTime))
            return false;
        if (connectionRequestTimeout == null) {
            if (other.connectionRequestTimeout != null)
                return false;
        } else if (!connectionRequestTimeout.equals(other.connectionRequestTimeout))
            return false;
        if (connectionTtl == null) {
            if (other.connectionTtl != null)
                return false;
        } else if (!connectionTtl.equals(other.connectionTtl))
            return false;
        if (contentCompressionEnabled == null) {
            if (other.contentCompressionEnabled != null)
                return false;
        } else if (!contentCompressionEnabled.equals(other.contentCompressionEnabled))
            return false;
        if (destroyDelayTime == null) {
            if (other.destroyDelayTime != null)
                return false;
        } else if (!destroyDelayTime.equals(other.destroyDelayTime))
            return false;
        if (expectContinueEnabled == null) {
            if (other.expectContinueEnabled != null)
                return false;
        } else if (!expectContinueEnabled.equals(other.expectContinueEnabled))
            return false;
        if (inactivityTimeBeforeValidate == null) {
            if (other.inactivityTimeBeforeValidate != null)
                return false;
        } else if (!inactivityTimeBeforeValidate.equals(other.inactivityTimeBeforeValidate))
            return false;
        if (ioThreadCount == null) {
            if (other.ioThreadCount != null)
                return false;
        } else if (!ioThreadCount.equals(other.ioThreadCount))
            return false;
        if (maxConnectionsPerRoute == null) {
            if (other.maxConnectionsPerRoute != null)
                return false;
        } else if (!maxConnectionsPerRoute.equals(other.maxConnectionsPerRoute))
            return false;
        if (maxRedirects == null) {
            if (other.maxRedirects != null)
                return false;
        } else if (!maxRedirects.equals(other.maxRedirects))
            return false;
        if (maxTotalConections == null) {
            if (other.maxTotalConections != null)
                return false;
        } else if (!maxTotalConections.equals(other.maxTotalConections))
            return false;
        if (redirectsEnabled == null) {
            if (other.redirectsEnabled != null)
                return false;
        } else if (!redirectsEnabled.equals(other.redirectsEnabled))
            return false;
        if (relativeRedirectsAllowed == null) {
            if (other.relativeRedirectsAllowed != null)
                return false;
        } else if (!relativeRedirectsAllowed.equals(other.relativeRedirectsAllowed))
            return false;
        if (retryIOExceptions == null) {
            if (other.retryIOExceptions != null)
                return false;
        } else if (!retryIOExceptions.equals(other.retryIOExceptions))
            return false;
        if (retryTimes == null) {
            if (other.retryTimes != null)
                return false;
        } else if (!retryTimes.equals(other.retryTimes))
            return false;
        if (socketTimeout == null) {
            if (other.socketTimeout != null)
                return false;
        } else if (!socketTimeout.equals(other.socketTimeout))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DynamicPoolingHttpClientProviderConfig [expectContinueEnabled=" + expectContinueEnabled
                + ", redirectsEnabled=" + redirectsEnabled + ", relativeRedirectsAllowed=" + relativeRedirectsAllowed
                + ", circularRedirectsAllowed=" + circularRedirectsAllowed + ", maxRedirects=" + maxRedirects
                + ", authenticationEnabled=" + authenticationEnabled + ", connectionRequestTimeout="
                + connectionRequestTimeout + ", connectTimeout=" + connectTimeout + ", socketTimeout=" + socketTimeout
                + ", contentCompressionEnabled=" + contentCompressionEnabled + ", connectionTtl=" + connectionTtl
                + ", connectionIdleTime=" + connectionIdleTime + ", cleanCheckInterval=" + cleanCheckInterval
                + ", inactivityTimeBeforeValidate=" + inactivityTimeBeforeValidate + ", maxConnectionsPerRoute="
                + maxConnectionsPerRoute + ", maxTotalConections=" + maxTotalConections + ", retryTimes=" + retryTimes
                + ", retryIOExceptions=" + retryIOExceptions + ", ioThreadCount=" + ioThreadCount
                + ", destroyDelayTime=" + destroyDelayTime + "]";
    }

}
