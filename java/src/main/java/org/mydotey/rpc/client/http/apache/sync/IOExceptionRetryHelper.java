package org.mydotey.rpc.client.http.apache.sync;

import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class IOExceptionRetryHelper {

    private static final Logger _logger = LoggerFactory.getLogger(IOExceptionRetryHelper.class);

    public static final IOExceptionRetryHelper DEFAULT = new DefaultIOExceptionRetryHelper();

    private int _retryTimes;

    private ConcurrentSkipListSet<Class<?>> _ioExceptionTypesToBeRetried = new ConcurrentSkipListSet<>(
            new Comparator<Class<?>>() {
                @Override
                public int compare(Class<?> o1, Class<?> o2) {
                    if (Objects.equals(o1, o2))
                        return 0;
                    if (o1 == null)
                        return -1;
                    if (o2 == null)
                        return 1;
                    return o1.getName().compareTo(o2.getName());
                }
            });

    private HttpRequestRetryHandler _retryHandler = new HttpRequestRetryHandler() {
        @Override
        public boolean retryRequest(IOException ex, int executionCount, HttpContext context) {
            if (executionCount > IOExceptionRetryHelper.this.getRetryTimes())
                return false;

            if (!IOExceptionRetryHelper.this.isRetriableIOException(ex))
                return false;

            _logger.info("Will retry. Execution count: {}, exception type: {}, exception message: {}", executionCount,
                    ex.getClass().getName(), ex.getMessage());
            return true;
        }
    };

    public IOExceptionRetryHelper(Class<?>... ioExceptionClasses) {
        this(1, ioExceptionClasses);
    }

    public IOExceptionRetryHelper(int retryTimes, Class<?>... ioExceptionClasses) {
        setRetryTimes(retryTimes);
        addIOExceptionsToBeRetried(ioExceptionClasses);
    }

    public int getRetryTimes() {
        return _retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        _retryTimes = retryTimes < 0 ? 1 : retryTimes;
    }

    public HttpRequestRetryHandler retryHandler() {
        return _retryHandler;
    }

    public void addIOExceptionsToBeRetried(Class<?>... ioExceptionClasses) {
        if (ioExceptionClasses == null || ioExceptionClasses.length == 0)
            return;

        for (Class<?> c : ioExceptionClasses) {
            if (c == null)
                continue;

            if (!IOException.class.isAssignableFrom(c)) {
                throw new IllegalArgumentException("Class " + c.getName() + " is not subclass of IOException");
            }

            _ioExceptionTypesToBeRetried.add(c);
        }
    }

    protected void removeIOExceptionsToBeRetried(Class<?>... ioExceptionClasses) {
        if (ioExceptionClasses == null || ioExceptionClasses.length == 0)
            return;

        for (Class<?> c : ioExceptionClasses) {
            if (c == null)
                continue;

            _ioExceptionTypesToBeRetried.remove(c);
        }
    }

    protected void clearIOExceptionsToBeRetried() {
        _ioExceptionTypesToBeRetried.clear();
    }

    protected boolean isRetriableIOException(IOException ex) {
        if (ex == null)
            return false;

        for (Class<?> c : _ioExceptionTypesToBeRetried) {
            if (c.isInstance(ex)) {
                return true;
            }
        }

        return false;
    }

    private static class DefaultIOExceptionRetryHelper extends IOExceptionRetryHelper {

        private boolean _inited;

        public DefaultIOExceptionRetryHelper() {
            super(NoHttpResponseException.class);
            _inited = true;
        }

        @Override
        public void setRetryTimes(int retryTimes) {
            checkDisabled();
            super.setRetryTimes(retryTimes);
        }

        @Override
        public void addIOExceptionsToBeRetried(Class<?>... ioExceptionClasses) {
            checkDisabled();
            super.addIOExceptionsToBeRetried(ioExceptionClasses);
        }

        @Override
        protected void removeIOExceptionsToBeRetried(Class<?>... ioExceptionClasses) {
            checkDisabled();
            super.removeIOExceptionsToBeRetried(ioExceptionClasses);
        }

        @Override
        protected void clearIOExceptionsToBeRetried() {
            checkDisabled();
            super.clearIOExceptionsToBeRetried();
        }

        private void checkDisabled() {
            if (_inited)
                throw new UnsupportedOperationException("DEFAULT retry helper cannot be changed.");
        }

    }

}
