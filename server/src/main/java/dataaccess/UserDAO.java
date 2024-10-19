package dataaccess;

import model.UserData;

public abstract class UserDAO implements DataAccess{

  @Override
  public void clear() {

  }
  @Override
  public void createUser(UserData user) throws DataAccessException {

  }

  @Override
  public UserData getUser() throws DataAccessException {
    return null;
  }
}
