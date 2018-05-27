package edu.drexel.se577.grouptwo.viz;

import spark.Spark;
import spark.Route;

public class Routing {
    private static Route getDefinitions = (request, reply) -> {
        reply.type("application/json");
        return Datasets.getInstance().all();
    };
    public static void main(String[] args) {
        Spark.staticFileLocation("/public");
        Spark.path("/api", () -> {
            Spark.path("/definitions", () -> {
                Spark.get("", Routing.getDefinitions);
            });
        });
        Spark.init();
    }
}
