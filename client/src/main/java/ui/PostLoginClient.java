package ui;

import server.ServerFacade;

public class PostLoginClient implements ClientInterface{
  private final ServerFacade serverFacade;

  public PostLoginClient(ServerFacade server) {
    serverFacade = server;
  }

  @Override
  public String eval(String input) {
    return null;
  }

  @Override
  public String help() {
    return null;
  }
}
