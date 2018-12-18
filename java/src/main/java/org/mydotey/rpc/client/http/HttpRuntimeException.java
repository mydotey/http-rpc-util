package org.mydotey.rpc.client.http;

/**
 * @author koqizhao
 *
 * Dec 18, 2018
 */
public class HttpRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public HttpRuntimeException() {
        super();
    }

    public HttpRuntimeException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public HttpRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpRuntimeException(String message) {
        super(message);
    }

    public HttpRuntimeException(Throwable cause) {
        super(cause);
    }

}
