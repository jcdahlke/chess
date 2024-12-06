package server.websocket;


import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.GameData;


public class Service {
  private final GameDataAccess gameDataAccess;

  private final AuthDataAccess authDataAccess;


  public Service(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
    this.gameDataAccess=gameDataAccess;
    this.authDataAccess=authDataAccess;
  }

  public String getUsername(String authToken) throws DataAccessException {
    return authDataAccess.getAuth(authToken).username();
  }

  public ChessGame getGame(int gameID) throws DataAccessException {
    return gameDataAccess.getGame(String.valueOf(gameID)).game();
  }

  public void makeMove(int gameID, ChessMove move) throws DataAccessException, InvalidMoveException {
    ChessGame game = gameDataAccess.getGame(String.valueOf(gameID)).game();
    game.makeMove(move);
    gameDataAccess.updateGameBoard(String.valueOf(gameID), game);
  }

  public ChessGame.TeamColor getUserColor(String authToken, int gameID) throws DataAccessException {
    GameData gameData = gameDataAccess.getGame(String.valueOf(gameID));
    String username = getUsername(authToken);
    if (gameData.whiteUsername() != null && gameData.whiteUsername().equals(username)) {
      return ChessGame.TeamColor.WHITE;
    }
    else if (gameData.blackUsername() != null && gameData.blackUsername().equals(username)) {
      return ChessGame.TeamColor.BLACK;
    }
    else {
      return null;
    }
  }

  public void leaveGame(int gameID, String authToken) throws DataAccessException {
    GameData gameData = gameDataAccess.getGame(String.valueOf(gameID));
    if(gameData.whiteUsername() != null && gameData.whiteUsername().equals(getUsername(authToken))) {
      gameDataAccess.updateGame(String.valueOf(gameID), null, ChessGame.TeamColor.WHITE);
    }
    else if (gameData.blackUsername() != null && gameData.blackUsername().equals(getUsername(authToken))) {
      gameDataAccess.updateGame(String.valueOf(gameID), null, ChessGame.TeamColor.BLACK);
    }
  }

  public void resignGame(int gameID, String authToken) throws DataAccessException {
    ChessGame game = gameDataAccess.getGame(String.valueOf(gameID)).game();
    ChessGame.TeamColor userColor = getUserColor(authToken, gameID);
    game.playerResign(userColor);
    gameDataAccess.updateGameBoard(String.valueOf(gameID), game);
  }
}
