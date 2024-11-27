package ui;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import server.ServerFacade;

import java.util.Arrays;

public class GameClient implements ClientInterface{
  private final ServerFacade serverFacade;
  private final String authToken;
  private final String username;

  public GameClient(ServerFacade server, String authToken, String username) {
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

    new DrawBoard(new ChessBoard(), "white");
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
  public String movePiece(String... params){
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
    return "moved piece from " + params[0] + " to " + params[1];

  }

  public String leaveGame() {
    return "";
  }

  public String resign() {
    return "";
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
    return "highlighted moves for piece in position " + params[0];
  }
  @Override
  public String help() {
    return null;
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