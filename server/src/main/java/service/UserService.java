package service;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.UserData;

public class UserService {
  private final UserDataAccess userDataAccess;
  private final AuthDataAccess authDataAccess;

  public UserService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
    this.userDataAccess=userDataAccess;
    this.authDataAccess=authDataAccess;
  }

  public AuthData register(String username, String password, String email) throws DataAccessException {
    if (userDataAccess.getUser(username) != null) {
      throw new DataAccessException("User already exists");
    }
    userDataAccess.createUser(username, password, email);
    String authToken =authDataAccess.createAuth(username);
    return authDataAccess.getAuth(authToken);
  }

  public AuthData userLogin(String username, String password) throws DataAccessException {
    UserData user = userDataAccess.getUser(username);
    if(user == null) {
      throw new DataAccessException("Invalid username");
    }
    if(userDataAccess.authenticateUser(password, user) && user != null ){
      String authToken = authDataAccess.createAuth(username);
      return authDataAccess.getAuth(authToken);
    }
    else {
      throw new DataAccessException("Wrong password");
    }
  }

  public void logout(String authToken) throws DataAccessException {
    authDataAccess.deleteAuth(authToken);
  }
}
