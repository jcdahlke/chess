package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public abstract class AuthDAO implements DataAccess{
  private final HashMap<String, AuthData> authData = new HashMap<>();
  @Override
  public void clear() {

  }

  @Override
  public void createAuth(String username) {
    AuthData auth = new AuthData(generateToken(), username);

    authData.put(auth.authToken(), auth);
  }

  @Override
  public AuthData getAuth(String authToken) throws DataAccessException {
    return authData.get(authToken);
  }

  @Override
  public void deleteAuth(String authToken) throws DataAccessException {
    authData.remove(authToken);
  }

  public static String generateToken() {
    return UUID.randomUUID().toString();
  }
}
