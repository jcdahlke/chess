package ui;

import chess.*;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import server.ServerFacade;
import static ui.EscapeSequences.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;


public class GameClient implements ClientInterface{
  private final ServerFacade serverFacade;
  private final String authToken;
  private final String username;
  private final ChessGame.TeamColor color;
  private final int gameIndex;
  private ChessGame game;

  public GameClient(ServerFacade server, String authToken, String username, ChessGame.TeamColor playerColor, int gameIndex) {
    serverFacade = server;
    this.authToken = authToken;
    this.username = username;
    color = playerColor;
    this.gameIndex = gameIndex -1;
    Collection<GameData> games = null;
    try {
      games = serverFacade.listGames(authToken);
    }
    catch (Exception ex) {
      System.out.println("problem getting game list.");
    }

    game = ((GameData)games.toArray()[this.gameIndex]).game();
    redraw();
  }


  @Override
  public String eval(String input) {
    try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "redraw" -> redraw();
        case "move" -> movePiece(params);
        case "leave" -> leaveGame();
        case "resign" -> resign();
        case "highlight" -> highlightPossibleMoves(params);
        default -> help();
      };
    } catch (Exception ex) {
      return ex.getMessage();
    }
  }


  public String redraw() {
    if (color == ChessGame.TeamColor.BLACK) {
      new DrawBoard(game.getBoard(), "black").displayBoard();
    }
    else {
      new DrawBoard(game.getBoard(), "white").displayBoard();
    }
    return "";
  }

  private boolean isWrongInput(char one, char two) {
    // Ensure one is a letter in [a-h] and the other is a digit in [1-8]
    boolean validOne = Character.isLetter(one) && Character.toString(one).matches("[a-h]");
    boolean validTwo = Character.isDigit(two) && Character.toString(two).matches("[1-8]");

    boolean validAltOne = Character.isLetter(two) && Character.toString(two).matches("[a-h]");
    boolean validAltTwo = Character.isDigit(one) && Character.toString(one).matches("[1-8]");

    // Valid if one pair is correct or the alternate pair is correct
    return !(validOne && validTwo || validAltOne && validAltTwo);
  }
  public String movePiece(String... params) throws InvalidMoveException {
    if (color == null) {
      return "Only players can make moves";
    }
    if (color != game.getTeamTurn()) {
      return "It is not your turn.";
    }

    if (params.length < 2 || params[0].length() != 2 || params[1].length() != 2) {
      return "Incorrect Input, please use <a-h><1-8> <a-h><1-8>";
    }
    int fromInnerIndex = 0;
    int fromOuterIndex = 0;
    int toInnerIndex = 0;
    int toOuterIndex = 0;
    char one = params[0].charAt(0);
    char two = params[0].charAt(1);
    char three = params[1].charAt(0);
    char four = params[1].charAt(1);
    if (isWrongInput(one,two) || isWrongInput(three,four)) {
      return "Incorrect Input, please use <a-h><1-8> <a-h><1-8>";
    }
    if (Character.isLetter(one)) {
      fromInnerIndex = (int)Character.toLowerCase(one) - 96;
    }
    if (Character.isLetter(two)) {
      fromInnerIndex = (int)Character.toLowerCase(two) - 96;
    }
    if (Character.isLetter(three)) {
      toInnerIndex = (int)Character.toLowerCase(three) - 96;
    }
    if (Character.isLetter(four)) {
      toInnerIndex = (int)Character.toLowerCase(four) - 96;
    }
    if (Character.isDigit(one)) {
      fromOuterIndex = Character.getNumericValue(one);
    }
    if (Character.isDigit(two)) {
      fromOuterIndex = Character.getNumericValue(two);
    }
    if (Character.isDigit(three)) {
      toOuterIndex = Character.getNumericValue(three);
    }
    if (Character.isDigit(four)) {
      toOuterIndex = Character.getNumericValue(four);
    }
    ChessPosition startPosition = new ChessPosition(fromOuterIndex, fromInnerIndex);
    ChessPosition endPosition = new ChessPosition(toOuterIndex, toInnerIndex);
    ChessMove chessMove = new ChessMove(startPosition, endPosition);
    game.makeMove(chessMove);
    return "moved piece from " + params[0] + " to " + params[1];

  }

  public String leaveGame() {
    return String.format("%s has left the game", username);
  }

  public String resign() {
    Scanner scanner = new Scanner(System.in);
    String result = "";
    while (result != "yes" || result != "no") {
      System.out.println("Are you sure you want to resign? <yes/no>");
      System.out.print(">>> ");
      result = scanner.nextLine();
      System.out.println();
    }
    if (result.equals("yes")) {
      return username + " resigned from the game";
    }
    else {
      return help();
    }
  }

  public String highlightPossibleMoves(String... params) {
    if (params.length < 1 || params[0].length() != 2) {
      return "Incorrect Input, please use <a-h><1-8>";
    }
    int innerIndex = 0;
    int outerIndex = 0;
    char one = params[0].charAt(0);
    char two = params[0].charAt(1);
    if (isWrongInput(one,two)) {
      return "Incorrect Input, please use <a-h><1-8>";
    }
    if (Character.isLetter(one)) {
      innerIndex = (int)Character.toLowerCase(one) - 96;
    }
    if (Character.isLetter(two)) {
      innerIndex = (int)Character.toLowerCase(two) - 96;
    }
    if (Character.isDigit(one)) {
      outerIndex = Character.getNumericValue(one);
    }
    if (Character.isDigit(two)) {
      outerIndex = Character.getNumericValue(two);
    }
    ChessPosition highlightPosition = new ChessPosition(outerIndex, innerIndex);
    ChessPiece piece = game.getBoard().getPiece(highlightPosition);
    if (piece == null) {
      return "There is no Chess Piece at this location";
    }
    Collection<ChessMove> possibleMoves = game.validMoves(highlightPosition);
    Collection<ChessPosition> possibleEndPositions = new ArrayList<>();
    for (ChessMove move: possibleMoves) {
      possibleEndPositions.add(move.getEndPosition());
    }
    if (color == ChessGame.TeamColor.BLACK) {
      new DrawBoard(game.getBoard(), "black", possibleEndPositions).displayBoard();
    }
    else {
      new DrawBoard(game.getBoard(), "white", possibleEndPositions).displayBoard();
    }

    return "highlighted moves for piece in position " + params[0];
  }
  @Override
  public String help() {
    System.out.print(SET_TEXT_COLOR_BLUE);
    return"""
          - redraw
          - move <a-h><1-8> <a-h><1-8>
          - leave
          - resign
          - highlight <a-h><1-8>
          - help
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

  public void getUpdatedGame() {
    Collection<GameData> games = null;
    try {
      games = serverFacade.listGames(authToken);
    }
    catch (Exception ex) {
      System.out.println("problem getting game list.");
    }

    game = ((GameData)games.toArray()[gameIndex]).game();
  }
}