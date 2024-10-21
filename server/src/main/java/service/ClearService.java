package service;

import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;

public class ClearService {
  private final GameDataAccess gameDataAccess;
  private final UserDataAccess userDataAccess;
  private final AuthDataAccess authDataAccess;

  public ClearService(GameDataAccess gameDataAccess, UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
    this.gameDataAccess=gameDataAccess;
    this.userDataAccess=userDataAccess;
    this.authDataAccess=authDataAccess;
  }

  public void clearAllData() {
    gameDataAccess.clear();
    userDataAccess.clear();
    authDataAccess.clear();
  }
}
