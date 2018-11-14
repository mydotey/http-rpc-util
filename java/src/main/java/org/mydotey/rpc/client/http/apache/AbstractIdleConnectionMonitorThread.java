package org.mydotey.rpc.client.http.apache;

import java.io.Closeable;
import java.lang.ref.WeakReference;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public abstract class AbstractIdleConnectionMonitorThread<T> extends Thread implements Closeable {

    private static final Logger _logger = LoggerFactory.getLogger(AbstractIdleConnectionMonitorThread.class);

    private WeakReference<T> _managerReference;
    private int _idleTime;
    private int _checkInterval;

    public AbstractIdleConnectionMonitorThread(T manager, int idleTime, int checkInterval) {
        Objects.requireNonNull(manager, "manager is null");
        if (idleTime <= 0)
            throw new IllegalArgumentException("idleTime <= 0");
        if (checkInterval <= 0)
            throw new IllegalArgumentException("checkInterval <= 0");

        _managerReference = new WeakReference<>(manager);
        _idleTime = idleTime;
        _checkInterval = checkInterval;

        setDaemon(true);
        setName("idle-connection-monitor-thread");
    }

    @Override
    public void run() {
        for (T m = _managerReference.get(); m != null; m = _managerReference.get()) {
            try {
                closeIdleConnection(m);
            } catch (Throwable ex) {
                _logger.warn("Error closing idle connections", ex);
            }

            try {
                Thread.sleep(_checkInterval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @Override
    public void close() {
        _managerReference.clear();
    }

    protected int idleTime() {
        return _idleTime;
    }

    protected abstract void closeIdleConnection(T manager);

}
