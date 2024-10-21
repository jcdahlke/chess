package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.UserData;

public class UserService {
  private final DataAccess dataAccess;

  public UserService(DataAccess dataAccess) {
    this.dataAccess=dataAccess;
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
}
