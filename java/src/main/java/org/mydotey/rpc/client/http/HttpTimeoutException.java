package org.mydotey.rpc.client.http;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class HttpTimeoutException extends HttpRuntimeException {

    private static final long serialVersionUID = 1L;

    public HttpTimeoutException() {

    }

    public HttpTimeoutException(String message) {
        super(message);
    }

    public HttpTimeoutException(Throwable ex) {
        super(ex);
    }

    public HttpTimeoutException(String message, Throwable ex) {
        super(message, ex);
    }

}
