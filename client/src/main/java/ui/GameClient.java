package ui;

import server.ServerFacade;

public class GameClient {
  private final ServerFacade serverFacade;

  public GameClient(ServerFacade server) {
    serverFacade = server;
  }
}
