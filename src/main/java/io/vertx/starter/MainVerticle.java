package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainVerticle extends AbstractVerticle {

    // Store our product
    private Map<Integer, Whisky> products = new LinkedHashMap<>();

    @Override
    public void start(Future<Void> future) {
        createSomeData();

        Router router = Router.router(vertx);
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("<h1>Hello Vert.x!</h1>");
        });

        // Serve static resources from the /assets directory
        router.route("/assets/*").handler(StaticHandler.create("assets"));

        //REST API
        router.route("/api/whiskies*").handler(BodyHandler.create());
        router.get("/api/whiskies").handler(this::getAll);
        router.post("/api/whiskies").handler(this::addOne);
        router.delete("/api/whiskies/:id").handler(this::deleteOne);
        router.put("/api/whiskies/:id").handler(this::updateOne);
        router.get("/api/whiskies/:id").handler(this::getOne);

        vertx.createHttpServer()
             .requestHandler(router::accept)
             .listen(config().getInteger("http.port", 8080),
                     result -> {
                         if (result.succeeded()) {
                             future.complete();
                         } else {
                             future.fail(result.cause());
                         }
                     });
    }

    // Create some product
    private void createSomeData() {
        Whisky bowmore = new Whisky("Bowmore 15 Years Laimrig", "Scotland, Islay");
        products.put(bowmore.getId(), bowmore);
        Whisky talisker = new Whisky("Talisker 57° North", "Scotland, Island");
        products.put(talisker.getId(), talisker);
    }

    private void getAll(RoutingContext routingContext) {
        routingContext.response()
                      .putHeader("content-type", "application/json; charset=utf-8")
                      .end(Json.encodePrettily(products.values()));
    }

    private void addOne(RoutingContext routingContext) {
        Whisky whisky = Json.decodeValue(routingContext.getBodyAsString(), Whisky.class);
        products.put(whisky.getId(), whisky);
        System.out.println("Persisted whisky: " + whisky);
        routingContext.response()
                      .setStatusCode(201)
                      .putHeader("content-type", "application/json; charset=utf-8")
                      .end(Json.encodePrettily(whisky));
    }

    private void deleteOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            Integer idInt = Integer.parseInt(id);
            if (products.get(idInt) == null) {
                System.err.println("Could not delete, no whiskey found with id: " + idInt);
                routingContext.response().setStatusCode(404).end();
            }
            products.remove(idInt);
        }
        routingContext.response().setStatusCode(204).end();
    }

    private void updateOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        Whisky whiskyFromRequest = Json.decodeValue(routingContext.getBodyAsString(), Whisky.class);
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            Integer idInt = Integer.parseInt(id);
            Whisky storedWhisky = products.get(idInt);
            if (storedWhisky == null) {
                routingContext.response().setStatusCode(404).end();
            }
            storedWhisky.setName(whiskyFromRequest.getName());
            storedWhisky.setOrigin(whiskyFromRequest.getOrigin());
            products.put(storedWhisky.getId(), storedWhisky);
        }
        routingContext.response().setStatusCode(204).end();
    }

    private void getOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            Integer idInt = Integer.parseInt(id);
            Whisky whisky = products.get(idInt);
            if (whisky == null) {
                routingContext.response().setStatusCode(404).end();
            }
            routingContext.response()
                          .setStatusCode(200)
                          .putHeader("content-type", "application/json; charset=utf-8")
                          .end(Json.encodePrettily(whisky));
        }
    }
}
