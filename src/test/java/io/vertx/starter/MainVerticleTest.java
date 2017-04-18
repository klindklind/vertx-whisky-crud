package io.vertx.starter;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
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
    public void server_should_respond(TestContext context) {
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

    @Test
    public void should_be_able_to_add_whiskey(TestContext context) {
        Async async = context.async();
        String json = Json.encodePrettily(new Whisky("Jameson", "Ireland"));
        String length = Integer.toString(json.length());

        vertx.createHttpClient().post(randomPort, "localhost", "/api/whiskies")
             .putHeader("content-type", "application/json")
             .putHeader("content-length", length)
             .handler(response -> {
                 context.assertEquals(response.statusCode(), 201);
                 context.assertTrue(response.headers().get("content-type").contains("application/json; charset=utf-8"));
                 response.bodyHandler(body -> {
                     Whisky whisky = Json.decodeValue(body.toString(), Whisky.class);
                     context.assertEquals(whisky.getName(), "Jameson");
                     context.assertEquals(whisky.getOrigin(), "Ireland");
                     context.assertNotNull(whisky.getId());
                     async.complete();
                 });
             })
             .write(json)
             .end();
    }



}