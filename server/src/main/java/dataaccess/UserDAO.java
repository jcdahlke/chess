package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Objects;

public class UserDAO implements UserDataAccess{
  private final HashMap<String, UserData> users = new HashMap<>();

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

  //This may be an unnecessary method as I can check this on the service level
  @Override
  public Boolean authenticateUser(String password, UserData user) throws DataAccessException {
    return Objects.equals(password, user.password());
  }

  @Override
  public int size(){
    return users.size();
  }
}
