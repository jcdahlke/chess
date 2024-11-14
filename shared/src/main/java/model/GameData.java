package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
  @Override
  public String toString() {
    String white = whiteUsername;
    String black = blackUsername;
    if (whiteUsername == null) {
      white = "";
    }
    if (blackUsername == null) {
      black = "";
    }
    return  "gameName = " + gameName + '\n' +
            "whiteUsername = " + white + '\n' +
            "blackUsername = " + black + '\n';
  }
}
