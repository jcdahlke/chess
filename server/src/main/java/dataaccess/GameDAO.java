package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public abstract class GameDAO implements DataAccess {
  private int nextId = 1;
  private final HashMap<Integer, GameData> chessGames = new HashMap<>();
  @Override
  public void clear() {
    chessGames.clear();
  }
  @Override
  public int createGame(String gameName) throws DataAccessException {
    GameData game = new GameData(nextId++, null, null, gameName, new ChessGame());
    chessGames.put(game.gameID(), game);
    return game.gameID();
  }

  @Override
  public GameData getGame(String gameID) throws DataAccessException {
    return chessGames.get(Integer.parseInt(gameID));
  }

  @Override
  public Collection<GameData> listGames() {
    return chessGames.values();
  }

  @Override
  public void updateGame(String gameID, String newID) throws DataAccessException {
    //FIXME
  }
}
