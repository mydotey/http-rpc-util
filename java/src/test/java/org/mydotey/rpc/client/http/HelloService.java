package org.mydotey.rpc.client.http;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author koqizhao
 *
 * Sep 21, 2018
 */
@RestController
@EnableAutoConfiguration
public class HelloService {

    @RequestMapping("/hello")
    public HelloResponse hello(@RequestBody HelloRequest request) {
        HelloResponse response = new HelloResponse();
        response.setData(request.getHello());
        return response;
    }

}
