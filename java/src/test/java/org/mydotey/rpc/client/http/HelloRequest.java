package org.mydotey.rpc.client.http;

/**
 * @author koqizhao
 *
 * Nov 8, 2018
 */
public class HelloRequest {

    private String hello;

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    @Override
    public String toString() {
        return "HelloRequest [hello=" + hello + "]";
    }

}
