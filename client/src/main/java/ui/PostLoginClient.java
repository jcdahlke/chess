package ui;

import model.GameData;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Collection;

public class PostLoginClient implements ClientInterface{
  private final ServerFacade serverFacade;
  private final String authToken;
  private final String username;

  public PostLoginClient(ServerFacade server, String authToken, String username) {
    serverFacade = server;
    this.authToken = authToken;
    this.username = username;
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

  public String createGame(String... params) throws Exception {
    if (params.length == 1){
      String gameName = params[0];
      int gameID = serverFacade.createGame(authToken, gameName);

      return String.format("%s created the Chess game %s, with gameID %d.", username, gameName, gameID);
    }
    throw new Exception("Expected 1 argument: <gameName>");
  }

  public String listGames() throws Exception{
    Collection<GameData> games = serverFacade.listGames(authToken);
    String result = "";
    if (games.isEmpty()) {
      return "There are no active games, create one to get started!";
    }
    int gameCount = 0;
    for (GameData game: games) {
      result += "Game " + gameCount + ":\n";
      result += game.toString();
      result += "\n\n";
      gameCount++;
    }
    return result;
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

  @Override
  public String getUsername() {
    return username;
  }
}


