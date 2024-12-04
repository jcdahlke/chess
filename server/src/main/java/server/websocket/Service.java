package server.websocket;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Service {
  private final GameDataAccess gameDataAccess;
  private final UserDataAccess userDataAccess;
  private final AuthDataAccess authDataAccess;


  public Service(GameDataAccess gameDataAccess, UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
    this.gameDataAccess=gameDataAccess;
    this.userDataAccess=userDataAccess;
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

  public void leaveGame() {

  }

  public void resignGame() {

  }
}
