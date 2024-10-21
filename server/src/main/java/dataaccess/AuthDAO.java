package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class AuthDAO implements AuthDataAccess{
  private final HashMap<String, AuthData> authData = new HashMap<>();
  @Override
  public void clear() {

  }

  @Override
  public String createAuth(String username) {
    AuthData auth = new AuthData(username, generateToken());

    authData.put(auth.authToken(), auth);
    return auth.authToken();
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
