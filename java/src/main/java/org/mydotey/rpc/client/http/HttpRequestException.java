package org.mydotey.rpc.client.http;

/**
 * @author koqizhao
 *
 * Dec 18, 2018
 */
public class HttpRequestException extends HttpRuntimeException {

    private static final long serialVersionUID = 1L;

    public HttpRequestException() {
        super();
    }

    public HttpRequestException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public HttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpRequestException(String message) {
        super(message);
    }

    public HttpRequestException(Throwable cause) {
        super(cause);
    }

}
