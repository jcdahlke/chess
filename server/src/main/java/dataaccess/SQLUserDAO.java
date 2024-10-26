package dataaccess;

import model.UserData;

public class SQLUserDAO implements UserDataAccess{
  @Override
  public void clear() {

  }

  @Override
  public void createUser(String username, String password, String email) throws DataAccessException {

  }

  @Override
  public UserData getUser(String username) throws DataAccessException {
    return null;
  }

  @Override
  public Boolean authenticateUser(String password, UserData user) throws DataAccessException {
    return null;
  }

  @Override
  public int size() {
    return 0;
  }
}
