package service;

import chess.ChessGame;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import model.GameData;

import java.util.Collection;

public class GameService {
  private final GameDataAccess gameDataAccess;
  private final AuthDataAccess authDataAccess;

  public GameService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
    this.gameDataAccess=gameDataAccess;
    this.authDataAccess=authDataAccess;
  }


  public Collection<GameData> listGames(String authToken) throws DataAccessException {
    authDataAccess.getAuth(authToken);
    return gameDataAccess.listGames();
  }

  public int createGame(String authToken, String gameName) throws DataAccessException {
    if (authDataAccess.getAuth(authToken) == null){
      throw new DataAccessException("");
    }
    return gameDataAccess.createGame(gameName);
  }

  public void joinGame(String authToken, String gameID, ChessGame.TeamColor color) throws DataAccessException {
    String username = authDataAccess.getAuth(authToken).username();
    gameDataAccess.updateGame(gameID, username, color);
  }


}
