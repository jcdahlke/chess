package dataaccess;

import model.AuthData;

public class SQLAuthDAO implements AuthDataAccess{
  @Override
  public void clear() {

  }

  @Override
  public String createAuth(String username) {
    return null;
  }

  @Override
  public AuthData getAuth(String authToken) throws DataAccessException {
    return null;
  }

  @Override
  public void deleteAuth(String authToken) throws DataAccessException {

  }

  @Override
  public int size() {
    return 0;
  }
}
