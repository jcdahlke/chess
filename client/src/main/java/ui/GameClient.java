package ui;

import server.ServerFacade;

public class GameClient implements ClientInterface{
  private final ServerFacade serverFacade;
  private final String authToken;
  private final String username;

  public GameClient(ServerFacade server, String authToken, String username) {
    serverFacade = server;
    this.authToken = authToken;
    this.username = username;
  }

  @Override
  public String eval(String input) {
    return null;
  }

  @Override
  public String help() {
    return null;
  }

  @Override
  public String getAuthToken() {
    return authToken;
  }

  @Override
  public String getUsername() {
    return username;
  }
}