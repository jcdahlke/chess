package dataaccess;

import model.UserData;

public interface UserDataAccess {

  void clear();
  void createUser(String username, String password, String email) throws DataAccessException;

  UserData getUser(String username) throws DataAccessException;

  Boolean authenticateUser(String password, UserData user) throws DataAccessException;
}
