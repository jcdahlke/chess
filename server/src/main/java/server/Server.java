package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    private final Gson serializer = new Gson();
    private final AuthDAO authDAO= new AuthDAO();
    private final GameDAO gameDAO= new GameDAO();
    private final UserDAO userDAO= new UserDAO();
    private final ClearService clearService = new ClearService(gameDAO, userDAO, authDAO);
    private final GameService gameService = new GameService(gameDAO, authDAO);
    private final UserService userService = new UserService(userDAO, authDAO);


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        String gamePath = "/game";
        Spark.delete("/db", this::clearHandler);
        Spark.post("/user", this::registerHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.get(gamePath, this::listGamesHandler);
        Spark.post(gamePath, this::createGamesHandler);
        Spark.put(gamePath, this::joinGameHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private String clearHandler(Request req, Response res){
        clearService.clearAllData();
        res.status(200);
        return "";
    }

    private Object registerHandler(Request req, Response res) throws DataAccessException {
//        if (req.body().length() != 3) {
//            throw new RuntimeException("We need 3 inputs");
//        }

        var newUser = serializer.fromJson(req.body(), UserData.class);
        AuthData authData = userService.register(newUser.username(), newUser.password(), newUser.email());
        res.status(200);
        return new Gson().toJson(authData);
    }

    private Object loginHandler(Request req, Response res) throws DataAccessException {
        var newUser = serializer.fromJson(req.body(), UserData.class);
        AuthData authData = userService.userLogin(newUser.username(), newUser.password());
        res.status(200);
        return new Gson().toJson(authData);
    }

    private Object logoutHandler(Request req, Response res) throws DataAccessException {
        userService.logout(req.headers("authorization"));
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
