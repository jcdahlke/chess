package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;

public class GameService {
  private final DataAccess dataAccess;

  public GameService(DataAccess dataAccess) {
    this.dataAccess=dataAccess;
  }

  public Collection<GameData> listGames(String authToken) throws DataAccessException {
    dataAccess.getAuth(authToken);
    return dataAccess.listGames();
  }

  public int createGame(String authToken, String gameName) throws DataAccessException {
    return dataAccess.createGame(gameName);
  }


}
