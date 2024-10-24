package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDataAccess {

  void clear();

  int createGame(String gameName) throws DataAccessException;

  GameData getGame(String gameID) throws DataAccessException;

  Collection<GameData> listGames();

  void updateGame(String gameID, String newUser, ChessGame.TeamColor color) throws DataAccessException;

  int size();
}
