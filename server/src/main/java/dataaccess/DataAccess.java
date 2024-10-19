package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public interface DataAccess {

  void clear();
  void createUser(UserData user) throws DataAccessException;

  UserData getUser() throws DataAccessException;

  void createGame(String gameName) throws DataAccessException;

  GameData getGame(String gameID) throws DataAccessException;

  Collection<GameData> listGames();

  void updateGame(String gameID, String newID) throws DataAccessException;

  void createAuth();

  AuthData getAuth(String authToken) throws DataAccessException;

  void deleteAuth(String authToken) throws DataAccessException;
}
