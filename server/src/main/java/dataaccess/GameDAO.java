package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public abstract class GameDAO implements DataAccess {
  private int nextId = 1;
  final private HashMap<Integer, ChessGame> chesGames = new HashMap<>();
  @Override
  public void clear() {
    chesGames.clear();
  }
  @Override
  public void createGame(GameData gameData) throws DataAccessException {

  }

  @Override
  public GameData getGame(String gameID) throws DataAccessException {
    return null;
  }

  @Override
  public Collection<ChessGame> listGames() {
    return null;
  }

  @Override
  public void updateGame(String gameID, String newID) throws DataAccessException {

  }
}
