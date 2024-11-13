package ui;

import server.ServerFacade;

public class PostLoginClient implements ClientInterface{
  private final ServerFacade serverFacade;
  private final String authToken;

  public PostLoginClient(ServerFacade server, String authToken) {
    serverFacade = server;
    this.authToken = authToken;
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
}
