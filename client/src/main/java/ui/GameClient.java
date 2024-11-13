package ui;

import server.ServerFacade;

public class GameClient implements ClientInterface{
  private final ServerFacade serverFacade;

  public GameClient(ServerFacade server) {
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
