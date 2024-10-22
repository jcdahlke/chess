package server;

import chess.ChessGame;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Spark;


import java.util.Collection;
import java.util.Map;

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
        if (!req.body().contains("username") || !req.body().contains("password") || !req.body().contains("email")) {
            res.status(400);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("message", "Error: bad request");
            return errorResponse;
        }
        var newUser = serializer.fromJson(req.body(), UserData.class);

        AuthData authData;
        try {
            authData=userService.register(newUser.username(), newUser.password(), newUser.email());
        }
        catch (DataAccessException e) {
            res.status(403);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("message", "Error: already taken");
            return errorResponse;
        }
        res.status(200);
        return new Gson().toJson(authData);
    }

    private Object loginHandler(Request req, Response res) throws DataAccessException {
        var newUser = serializer.fromJson(req.body(), UserData.class);
        AuthData authData;
        try{
            authData = userService.userLogin(newUser.username(), newUser.password());
        }
        catch (DataAccessException e) {
            res.status(401);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("message", "Error: unauthorized");
            return errorResponse;
        }
        res.status(200);
        return new Gson().toJson(authData);
    }

    private Object logoutHandler(Request req, Response res) throws DataAccessException {
        try {
            userService.logout(req.headers("authorization"));
        }
        catch (DataAccessException e) {
            res.status(401);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("message", "Error: unauthorized");
            return errorResponse;
        }
        res.status(200);
        return "";
    }

    private Object listGamesHandler(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        Collection<GameData> gamesList;
        try {
            gamesList = gameService.listGames(authToken);
        }
        catch (DataAccessException e){
            res.status(401);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("message", "Error: unauthorized");
            return errorResponse;
        }

        res.status(200);
        return new Gson().toJson(Map.of("games", gamesList.toArray()));
    }

    private Object createGamesHandler(Request req, Response res) throws DataAccessException {
        if (!req.body().contains("gameName")) {
            res.status(400);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("message", "Error: bad request");
            return errorResponse;
        }
//        String gameName = serializer.fromJson(req.body(), String.class);
        JsonObject jsonObject = JsonParser.parseString(req.body()).getAsJsonObject();
        // Extract the "gameName" field from the JSON object
        String gameName = jsonObject.get("gameName").getAsString();
        int gameID;
        try {
            gameID=gameService.createGame(req.headers("authorization"), gameName);
        }
        catch (DataAccessException e) {
            res.status(401);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("message", "Error: unauthorized");
            return errorResponse;
        }
        res.status(200);
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("gameID", gameID);
        return responseJson.toString();

    }

    private Object joinGameHandler(Request req, Response res) throws DataAccessException {
        if (!req.body().contains("playerColor") || (!req.body().contains("WHITE") && !req.body().contains("BLACK")) || !req.body().contains("gameID")) {
            res.status(400);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("message", "Error: bad request");
            return errorResponse;
        }
        String authToken = req.headers("authorization");
        if (authDAO.getAuth(authToken)==null) {
            res.status(401);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("message", "Error: unauthorized");
            return errorResponse;
        }
        JsonObject jsonObject = JsonParser.parseString(req.body()).getAsJsonObject();
        String gameID = jsonObject.get("gameID").getAsString();
        String colorString = jsonObject.get("playerColor").getAsString();
        ChessGame.TeamColor color;
        if (colorString.equals("WHITE")) {
            color = ChessGame.TeamColor.WHITE;
        }
        else {
            color = ChessGame.TeamColor.BLACK;
        }
        try {
            gameService.joinGame(authToken, gameID, color);
        }
        catch (DataAccessException e) {
            res.status(403);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("message", "Error: already taken");
            return errorResponse;
        }
        res.status(200);
        return "";
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
