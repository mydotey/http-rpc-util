package org.mydotey.rpc.client.http;

/**
 * @author koqizhao
 *
 * Nov 8, 2018
 */
public class HelloResponse {

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HelloResponse [data=" + data + "]";
    }

}
