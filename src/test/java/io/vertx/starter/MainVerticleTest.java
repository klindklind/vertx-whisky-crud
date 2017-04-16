package io.vertx.starter;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.ServerSocket;

@RunWith(VertxUnitRunner.class)
public class MainVerticleTest {

    Vertx vertx;
    int randomPort;

    @Before
    public void setUp(TestContext context) throws IOException {
        vertx = Vertx.vertx();

        //Get random randomPort
        ServerSocket socket = new ServerSocket(0);
        randomPort = socket.getLocalPort();
        socket.close();

        DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port", randomPort));
        vertx.deployVerticle(MainVerticle.class.getName(), options, context.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void testThatTheServerIsStarted(TestContext context) {
        Async async = context.async();

        vertx.createHttpClient().getNow(randomPort, "localhost", "/", response -> {
            context.assertEquals(response.statusCode(), 200);
            response.bodyHandler(body -> {
                String expectedString = "Hello";
                context.assertTrue(body.toString().contains(expectedString),
                                   "Could not find string '" + expectedString + "' from body: " + body.toString());
                async.complete();
            });
        });
    }

}