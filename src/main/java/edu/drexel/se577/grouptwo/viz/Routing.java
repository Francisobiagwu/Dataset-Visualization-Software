package edu.drexel.se577.grouptwo.viz;

import spark.Spark;
import spark.Route;
import java.net.URI;
import java.util.stream.Stream;

public class Routing {
    private static Route getDefinitions = (request, reply) -> {
        reply.type("application/json");
        return Datasets.getInstance().all();
    };

    private static Route postDefinition = (request, reply) -> {
        URI id = Datasets.getInstance().createDataset(request.body());
        URI location = URI.create("/api/datasets/").resolve(id);
        reply.header("Location", location.toString());
        reply.status(201);
        return "";
    };

    private static Route getDataset = (request, reply) -> {
        String id = request.params(":id");
        reply.type("application/json");
        return Datasets.getInstance().getDataset(id);
    };


    public static void main(String[] args) {
        Spark.staticFileLocation("/public");
        Spark.path("/api", () -> {
            Spark.path("/datasets", () -> {
                Spark.get("", Routing.getDefinitions);
                Spark.post("", Routing.postDefinition);
                Spark.get("/:id", Routing.getDataset);
            });
        });
        Spark.init();
    }
}
