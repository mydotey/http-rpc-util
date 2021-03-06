package org.mydotey.rpc.client.http.apache;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mydotey.rpc.client.http.HelloApp;
import org.mydotey.rpc.client.http.HelloRequest;
import org.mydotey.rpc.client.http.HelloResponse;
import org.mydotey.scf.ConfigurationManager;
import org.mydotey.scf.ConfigurationManagerConfig;
import org.mydotey.scf.facade.ConfigurationManagers;
import org.mydotey.scf.facade.SimpleConfigurationSources;
import org.mydotey.scf.source.stringproperty.memorymap.MemoryMapConfigurationSource;

/**
 * @author koqizhao
 *
 * Nov 8, 2018
 */
public class ApacheHttpRpcClientTest {

    private HelloApp _app;
    private TestApacheHttpRpcClient _client;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    protected int getServerPort() {
        return 8085;
    }

    @Before
    public void setUp() {
        _app = new HelloApp(getServerPort());
        _app.start();

        String serviceId = "hello-app";
        MemoryMapConfigurationSource configurationSource = SimpleConfigurationSources.newMemoryMapSource("memory-map");
        configurationSource.setPropertyValue(serviceId + ".service.url", "http://localhost:" + getServerPort());
        ConfigurationManagerConfig managerConfig = ConfigurationManagers.newConfigBuilder()
            .setName("ApacheHttpRpcClientTest").addSource(1, configurationSource).build();
        ConfigurationManager configurationManager = ConfigurationManagers.newManager(managerConfig);
        _client = new TestApacheHttpRpcClient(serviceId, configurationManager);
    }

    @After
    public void tearDown() throws IOException {
        _app.stop();
        _client.close();
    }

    @Test
    public void testHello() {
        HelloRequest request = new HelloRequest();
        request.setHello("Hello!");
        HelloResponse response = _client.invoke("hello", request, HelloResponse.class);
        System.out.println(response);
        Assert.assertEquals(request.getHello(), response.getData());
    }

    @Test
    public void testHello2() {
        expectedException.expect(ApacheHttpRequestException.class);

        HelloRequest request = new HelloRequest();
        request.setHello("Hello!");
        HelloResponse response = _client.invoke("hello2", request, HelloResponse.class);
        System.out.println(response);
        Assert.assertEquals(request.getHello(), response.getData());
    }

    @Test
    public void testHelloAsync() throws InterruptedException, ExecutionException {
        HelloRequest request = new HelloRequest();
        request.setHello("Hello!");
        CompletableFuture<HelloResponse> future = _client.invokeAsync("hello", request, HelloResponse.class);
        HelloResponse response = future.get();
        System.out.println(response);
        Assert.assertEquals(request.getHello(), response.getData());
    }

    @Test
    public void testHelloAsync2() throws Throwable {
        expectedException.expect(ApacheHttpRequestException.class);

        HelloRequest request = new HelloRequest();
        request.setHello("Hello!");
        CompletableFuture<HelloResponse> future = _client.invokeAsync("hello2", request, HelloResponse.class);
        try {
            HelloResponse response = future.get();
            System.out.println(response);
            Assert.assertEquals(request.getHello(), response.getData());
        } catch (ExecutionException e) {
            throw e.getCause();
        }
    }

}
