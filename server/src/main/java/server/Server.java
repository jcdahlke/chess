package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.delete("/db", this::clearHandler);
        Spark.post("/user", this::registerHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.get("/game", this::listGamesHandler);
        Spark.post("/game", this::createGamesHandler);
        Spark.put("/game", this::joinGameHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private String clearHandler(Request req, Response res){
        return "";
    }

    private Object registerHandler(Request req, Response res) {
        return "";
    }

    private Object loginHandler(Request req, Response res) {
        return "";
    }

    private Object logoutHandler(Request req, Response res) {
        return "";
    }

    private Object listGamesHandler(Request req, Response res) {
        return "";
    }

    private Object createGamesHandler(Request req, Response res) {
        return "";
    }

    private Object joinGameHandler(Request req, Response res) {
        return "";
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
