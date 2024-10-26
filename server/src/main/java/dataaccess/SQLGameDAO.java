package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public class SQLGameDAO implements GameDataAccess{
  @Override
  public void clear() {

  }

  @Override
  public int createGame(String gameName) throws DataAccessException {
    return 0;
  }

  @Override
  public GameData getGame(String gameID) throws DataAccessException {
    return null;
  }

  @Override
  public Collection<GameData> listGames() {
    return null;
  }

  @Override
  public void updateGame(String gameID, String newUser, ChessGame.TeamColor color) throws DataAccessException {

  }

  @Override
  public int size() {
    return 0;
  }
}
