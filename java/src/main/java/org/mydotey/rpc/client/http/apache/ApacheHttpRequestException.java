package org.mydotey.rpc.client.http.apache;

import java.io.InputStream;
import java.util.Objects;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;
import org.mydotey.rpc.client.http.HttpRequestException;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class ApacheHttpRequestException extends HttpRequestException {

    private static final long serialVersionUID = 1L;

    private Integer _statusCode;
    private String _reasonPhrase;
    private ProtocolVersion _protocolVersion;
    private Header[] _responseHeaders;
    private String _responseBody;

    public ApacheHttpRequestException(String message) {
        super(message);
    }

    public ApacheHttpRequestException(Throwable cause) {
        super(cause);
    }

    public ApacheHttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApacheHttpRequestException(HttpResponse response) {
        super(toErrorMessage(response));

        StatusLine statusLine = response.getStatusLine();
        if (statusLine == null)
            return;

        _statusCode = statusLine.getStatusCode();
        _reasonPhrase = statusLine.getReasonPhrase();
        _protocolVersion = statusLine.getProtocolVersion();
        _responseHeaders = response.getAllHeaders();

        HttpEntity entity = response.getEntity();
        if (entity == null)
            return;

        try (InputStream is = entity.getContent();) {
            _responseBody = EntityUtils.toString(entity);
        } catch (Throwable ex) {

        }
    }

    public Integer statusCode() {
        return _statusCode;
    }

    public String reasonPhrase() {
        return _reasonPhrase;
    }

    public ProtocolVersion protocolVersion() {
        return _protocolVersion;
    }

    public Header[] responseHeaders() {
        return _responseHeaders;
    }

    public String responseBody() {
        return _responseBody;
    }

    private static String toErrorMessage(HttpResponse response) {
        Objects.requireNonNull(response, "response is null");

        StatusLine statusLine = response.getStatusLine();
        if (statusLine == null)
            return "status line is null";

        return String.format("Http request failed. status code: %s, reason phrase: %s, protocol version: %s",
                statusLine.getStatusCode(), statusLine.getReasonPhrase(), statusLine.getProtocolVersion());
    }

}
