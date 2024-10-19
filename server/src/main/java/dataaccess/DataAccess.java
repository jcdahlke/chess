package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public interface DataAccess {

  void clear();
  void createUser(String username, String password, String email) throws DataAccessException;

  UserData getUser(String username) throws DataAccessException;

  Boolean authenticateUser(String password, UserData user) throws DataAccessException;

  void createGame(String gameName) throws DataAccessException;

  GameData getGame(String gameID) throws DataAccessException;

  Collection<GameData> listGames();

  void updateGame(String gameID, String newID) throws DataAccessException;

  void createAuth();

  AuthData getAuth(String authToken) throws DataAccessException;

  void deleteAuth(String authToken) throws DataAccessException;
}
