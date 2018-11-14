package org.mydotey.rpc.client.http.apache.async;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class AutoCleanedPoolingNHttpClientConnectionManager extends PoolingNHttpClientConnectionManager {

    private IdleNConnectionMonitorThread _idleNConnectionMonitorThread;

    public AutoCleanedPoolingNHttpClientConnectionManager(int ioThreadCount, int connectionTtl, int connectionIdleTime,
            int cleanCheckInterval) {
        super(createConnectingIOReactor(ioThreadCount), null, getDefaultRegistry(), null, null, connectionTtl,
                TimeUnit.MILLISECONDS);

        if (cleanCheckInterval > 0) {
            _idleNConnectionMonitorThread = new IdleNConnectionMonitorThread(this, connectionIdleTime,
                    cleanCheckInterval);
            _idleNConnectionMonitorThread.start();
        }
    }

    @Override
    public void shutdown() throws IOException {
        if (_idleNConnectionMonitorThread != null)
            _idleNConnectionMonitorThread.close();
        super.shutdown();
    }

    protected static ConnectingIOReactor createConnectingIOReactor(int ioThreadCount) {
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setIoThreadCount(ioThreadCount).setTcpNoDelay(true)
                .build();
        try {
            return new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected static Registry<SchemeIOSessionStrategy> getDefaultRegistry() {
        return RegistryBuilder.<SchemeIOSessionStrategy> create().register("http", NoopIOSessionStrategy.INSTANCE)
                .register("https", SSLIOSessionStrategy.getDefaultStrategy()).build();
    }

}
