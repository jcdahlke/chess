package dataaccess;

import model.AuthData;

public abstract class AuthDAO implements DataAccess{
  @Override
  public void clear() {

  }

  @Override
  public void createAuth() {

  }

  @Override
  public AuthData getAuth(String authToken) throws DataAccessException {
    return null;
  }

  @Override
  public void deleteAuth(String authToken) throws DataAccessException {

  }
}
