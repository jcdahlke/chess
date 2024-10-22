package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class GameDAO implements GameDataAccess {
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
  public void updateGame(String gameID, String newUser, ChessGame.TeamColor color) throws DataAccessException {
    GameData game = chessGames.get(Integer.parseInt(gameID));
    GameData newGame;
    if (color.equals(ChessGame.TeamColor.WHITE)){
      newGame = new GameData(game.gameID(), newUser, game.blackUsername(), game.gameName(), game.game());
    }
    else {
      newGame = new GameData(game.gameID(), game.whiteUsername(), newUser, game.gameName(), game.game());
    }
    chessGames.remove(Integer.parseInt(gameID));
    chessGames.put(Integer.parseInt(gameID), newGame);
  }

  @Override
  public int size(){
    return chessGames.size();
  }
}
