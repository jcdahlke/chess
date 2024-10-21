package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class UserService {
  private final DataAccess dataAccess;

  public UserService(DataAccess dataAccess) {
    this.dataAccess=dataAccess;
  }

  public AuthData register(String username, String password, String email) throws DataAccessException {
    if (dataAccess.getUser(username).equals(null)) {
      throw new DataAccessException("User already exists");
    }
    dataAccess.createUser(username, password, email);
    String authToken =dataAccess.createAuth(username);
    return dataAccess.getAuth(authToken);
  }

  public String userLogin(String username, String password) throws DataAccessException {
    UserData user = dataAccess.getUser(username);
    if(dataAccess.authenticateUser(password, user)){
      return dataAccess.createAuth(username);
    }
    else {
      throw new DataAccessException("WrongPassword");
    }
  }

  public void logout(String authToken) throws DataAccessException {
    dataAccess.deleteAuth(authToken);
  }
}
