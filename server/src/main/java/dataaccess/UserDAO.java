package dataaccess;

import model.UserData;

import java.util.HashMap;

public abstract class UserDAO implements DataAccess{
  final private HashMap<String, UserData> users = new HashMap<>();

  @Override
  public void clear() {
    users.clear();
  }
  @Override
  public void createUser(String username, String password, String email) throws DataAccessException {
    UserData user = new UserData(username, password, email);

    users.put(username, user);
  }

  @Override
  public UserData getUser(String username) throws DataAccessException {
    return users.get(username);
  }

  @Override
  public Boolean authenticateUser(String password, UserData user) throws DataAccessException {
    return password == user.password();
  }
}
