package server;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

public class ServerFacade {

  private final String serverUrl;

  public ServerFacade(String url) {
    serverUrl = url;
    System.out.println(serverUrl);
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


  private <T> T makeRequest(String method, String path, Object request, String authToken, Class<T> responseClass) throws Exception {
    try {
      URL url = (new URI(serverUrl + path)).toURL();
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      http.setRequestMethod(method);
      if (authToken != null) {
        http.addRequestProperty("Authorization", authToken);
      }
      http.setDoOutput(method != "GET");

      if (method != "GET") {
        writeBody(request, http);
      }

      http.connect();
      throwIfNotSuccessful(http);
      return readBody(http, responseClass);
    } catch (Exception ex) {
      throw new Exception(ex.getMessage());
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
    var status = http.getResponseCode();
    if (!isSuccessful(status)) {
      throw new Exception("failure: " + status);
    }
  }

  private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
    T response = null;
    if (http.getContentLength() < 0) {
      try (InputStream respBody = http.getInputStream()) {
        InputStreamReader reader = new InputStreamReader(respBody);
        if (responseClass != null) {
          response = new Gson().fromJson(reader, responseClass);
        }
      }
    }
    return response;
  }


  private boolean isSuccessful(int status) {
    return status / 100 == 2;
  }
}
