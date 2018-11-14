package org.mydotey.rpc.client.http.apache.sync;

import java.util.concurrent.TimeUnit;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class AutoCleanedPoolingHttpClientConnectionManager extends PoolingHttpClientConnectionManager {

    private static Registry<ConnectionSocketFactory> getDefaultRegistry() {
        return RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
    }

    private IdleConnectionMonitorThread _idleConnectionMonitorThread;

    public AutoCleanedPoolingHttpClientConnectionManager(int connectionTtl, int inactivityTimeBeforeValidate,
            int connectionIdleTime, int cleanCheckInterval) {
        super(getDefaultRegistry(), null, null, null, connectionTtl, TimeUnit.MILLISECONDS);

        setValidateAfterInactivity(inactivityTimeBeforeValidate);

        if (cleanCheckInterval > 0) {
            _idleConnectionMonitorThread = new IdleConnectionMonitorThread(this, connectionIdleTime,
                    cleanCheckInterval);
            _idleConnectionMonitorThread.start();
        }
    }

    @Override
    public void shutdown() {
        if (_idleConnectionMonitorThread != null)
            _idleConnectionMonitorThread.close();

        super.shutdown();
    }

}
