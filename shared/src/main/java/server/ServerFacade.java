package server;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Collection;

@ClientEndpoint
public class ServerFacade {

  private final String serverUrl;
  private final String socketUrl;
  private Session session;

  public ServerFacade(String url) {
    serverUrl = url;
    socketUrl = url.replace("http", "ws") + "/ws";

    try {
      // Connect to the WebSocket server
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      container.connectToServer(this, URI.create(socketUrl));
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException("Failed to initialize WebSocket connection: " + ex.getMessage());
    }

    System.out.println("Server URL: " + serverUrl);
  }

  @OnOpen
  public void onOpen(Session session) {
    this.session = session;
    System.out.println("Connected to WebSocket server!");
  }

  @OnMessage
  public void onMessage(String message) {
    System.out.println("Received WebSocket message: " + message);
    // Handle messages received from the server
  }

  @OnClose
  public void onClose(Session session, CloseReason reason) {
    System.out.println("WebSocket connection closed: " + reason);
    this.session = null;
  }

  @OnError
  public void onError(Session session, Throwable throwable) {
    System.err.println("WebSocket error: " + throwable.getMessage());
    throwable.printStackTrace();
  }

  public AuthData register(String username, String password, String email) throws Exception {
    String path = "/user";
    return this.makeRequest("POST", path, new UserData(username, password, email), null, AuthData.class);
  }

  public void clear() throws Exception {
    String path = "/db";
    this.makeRequest("DELETE", path, null, null, null);
  }

  public AuthData login(String username, String password) throws Exception {
    String path = "/session";
    return this.makeRequest("POST", path, new UserData(username, password, null), null, AuthData.class);
  }

  public void logout(String authToken) throws Exception {
    String path = "/session";
    this.makeRequest("DELETE", path, "", authToken, null);
  }

  public Collection<GameData> listGames(String authToken) throws Exception {
    String path = "/game";
    record ListGamesResponse(Collection<GameData> games) {}
    var response = this.makeRequest("GET", path, "", authToken, ListGamesResponse.class);
    return response.games;
  }

  public Integer createGame(String authToken, String gameName) throws Exception {
    String path = "/game";
    record CreateGameRequest(String gameName) {}
    record CreateGameResponse(int gameID) {}
    CreateGameRequest request = new CreateGameRequest(gameName);
    var response = this.makeRequest("POST", path, request, authToken, CreateGameResponse.class);
    return response.gameID;
  }

  public void joinGame(String authToken, String gameID, String playerColor) throws Exception {
    String path = "/game";
    record JoinGameRequest(String gameID, String playerColor) {}
    JoinGameRequest request = new JoinGameRequest(gameID, playerColor);
    this.makeRequest("PUT", path, request, authToken, null);

    try {
      var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, Integer.parseInt(gameID));
      if (this.session != null && this.session.isOpen()) {
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
      } else {
        throw new IOException("WebSocket session is not open");
      }
    } catch (IOException ex) {
      throw new Exception("Failed to send WebSocket command: " + ex.getMessage());
    }
  }

  public void leaveGame(String authToken, String gameID) throws Exception {
    try {
      var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, Integer.parseInt(gameID));
      if (this.session != null && this.session.isOpen()) {
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
      } else {
        throw new IOException("WebSocket session is not open");
      }
    } catch (IOException ex) {
      throw new Exception("Failed to send WebSocket command: " + ex.getMessage());
    }
  }

  private <T> T makeRequest(String method, String path, Object request, String authToken, Class<T> responseClass) throws Exception {
    try {
      var url = URI.create(serverUrl + path).toURL();
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      http.setRequestMethod(method);
      if (authToken != null) {
        http.addRequestProperty("Authorization", authToken);
      }
      http.setDoOutput(!method.equals("GET"));

      if (!method.equals("GET")) {
        writeBody(request, http);
      }

      http.connect();
      throwIfNotSuccessful(http);
      return readBody(http, responseClass);
    } catch (Exception ex) {
      throw new Exception("HTTP request failed: " + ex.getMessage());
    }
  }

  private static void writeBody(Object request, HttpURLConnection http) throws IOException {
    if (request != null) {
      http.addRequestProperty("Content-Type", "application/json");
      String reqData = new Gson().toJson(request);
      try (OutputStream reqBody = http.getOutputStream()) {
        reqBody.write(reqData.getBytes());
      }
    }
  }

  private void throwIfNotSuccessful(HttpURLConnection http) throws Exception {
    int status = http.getResponseCode();
    if (!isSuccessful(status)) {
      throw new Exception("HTTP failure: " + status);
    }
  }

  private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
    T response = null;
    if (http.getContentLength() > 0 && responseClass != null) {
      try (InputStream respBody = http.getInputStream()) {
        InputStreamReader reader = new InputStreamReader(respBody);
        response = new Gson().fromJson(reader, responseClass);
      }
    }
    return response;
  }

  private boolean isSuccessful(int status) {
    return status / 100 == 2;
  }
}
