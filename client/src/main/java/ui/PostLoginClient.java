package ui;

import server.ServerFacade;

public class PostLoginClient {
  private final ServerFacade serverFacade;

  public PostLoginClient(ServerFacade server) {
    serverFacade = server;
  }
}
