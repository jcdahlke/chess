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

      return String.format("%s created the Chess game %s, check list to get the game number", username, gameName, gameID);
    }
    throw new Exception("Expected 1 argument: <gameName>");
  }

  public String listGames() throws Exception{
    Collection<GameData> games = serverFacade.listGames(authToken);
    String result = "";
    if (games.isEmpty()) {
      return "There are no active games, create one to get started!";
    }
    int gameCount = 1;
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
      Collection<GameData> games = serverFacade.listGames(authToken);
      int gameID = 0;
      try {
        int gameIndex = Integer.parseInt(params[0]) - 1; // Convert input to 0-based index

        // Check if the gameIndex is within the valid range
        if (gameIndex < 0 || gameIndex >= games.size()) {
          throw new IndexOutOfBoundsException("Invalid game number. Please choose a number between 1 and " + games.size());
        }

        gameID = ((GameData) games.toArray()[gameIndex]).gameID();
        // Proceed with using the gameID as needed

      } catch (NumberFormatException e) {
        System.out.println("Error: Please enter a valid number.");
      } catch (IndexOutOfBoundsException e) {
        System.out.println("Error: " + e.getMessage());
      }
      try {
        switch (params[1].toLowerCase()) {
          case "white" -> {serverFacade.joinGame(authToken, String.valueOf(gameID), "WHITE");
          }
          case "black" -> serverFacade.joinGame(authToken, String.valueOf(gameID), "BLACK");
          default -> {
            return "The second argument must specify WHITE or BLACK";
          }
        }
      }
      catch (Throwable e) {
        String msg = e.toString();
        if (msg.split(" ")[2].equals("403")) {
          msg = "There is already a user for " + params[1].toUpperCase() + " in game " + params[0];
        }
        else if
        (msg.split(" ")[2].equals("500")) {
          msg = "There is no game number " + params[0];
        }
        return msg;
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
    throw new Exception("Expected 1 argument: <gameNumber>");
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
          - play <gameNumber> [WHITE|BLACK]
          - observe <gameNumber>
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


