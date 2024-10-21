package dataaccess;

import model.AuthData;

public interface AuthDataAccess {

  void clear();

  String createAuth(String username);

  AuthData getAuth(String authToken) throws DataAccessException;

  void deleteAuth(String authToken) throws DataAccessException;
}
