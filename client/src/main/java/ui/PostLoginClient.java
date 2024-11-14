package ui;

import chess.ChessBoard;
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
      result += "\n";
      gameCount++;
    }
    return result;
  }

  public String joinGame(String... params) throws Exception {
    if (params.length == 2) {
      switch (params[1].toLowerCase()) {
        case "white" -> serverFacade.joinGame(authToken, params[0], "WHITE");
        case "black" -> serverFacade.joinGame(authToken, params[0], "BLACK");
        default -> {
          return "The second argument must specify WHITE or BLACK";
        }
      }
      ChessBoard board = new ChessBoard();
      board.resetBoard();
      new DrawBoard(board,"white").displayBoard();
      new DrawBoard(board,"black").displayBoard();
      return String.format("%s has successfully joined game %s playing %s", username, params[0], params[1].toUpperCase());
    }
    throw new Exception("Expected 2 arguments: <gameID> <WHITE|BLACK>");
  }

  public String observeGame(String... params) throws Exception {
    if (params.length == 1) {
      ChessBoard board = new ChessBoard();
      board.resetBoard();
      new DrawBoard(board,"white").displayBoard();
      new DrawBoard(board,"black").displayBoard();
      return String.format("%s is observing game %s", username, params[0]);
    }
    throw new Exception("Expected 1 argument: <gameID>");
  }

  public String logout() throws Exception {
    serverFacade.logout(authToken);
    return  String.format("%s has successfully logged out", username);
  }

  @Override
  public String help() {
    return"""
          - create <gameName>
          - list
          - play <gameID> [WHITE|BLACK]
          - observe <gameID>
          - logout
          - help
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


