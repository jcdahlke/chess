package ui;

import server.ServerFacade;

import java.util.Arrays;

public class PostLoginClient implements ClientInterface{
  private final ServerFacade serverFacade;
  private final String authToken;

  public PostLoginClient(ServerFacade server, String authToken) {
    serverFacade = server;
    this.authToken = authToken;
  }

  @Override
  public String eval(String input) {
    try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "create" -> createGame(params);
        case "list" -> listGames();
        case "play" -> joinGame(params);
        case "observe" -> observeGame(params);
        case "logout" -> logout();
        case "quit" -> "quit";
        default -> help();
      };
    } catch (Exception ex) {
      return ex.getMessage();
    }
  }

  public String createGame(String... params) {
    return null;
  }

  public String listGames() {
    return null;
  }

  public String joinGame(String... params) {
    return null;
  }

  public String observeGame(String... params) {
    return null;
  }

  public String logout() {
    return null;
  }

  @Override
  public String help() {
    return"""
          - create <gameName>
          - list
          - play <gameID> [WHITE|BLACK]
          - observe <gameID>
          - logout
          - quit
          """;
  }

  @Override
  public String getAuthToken() {
    return authToken;
  }
}
