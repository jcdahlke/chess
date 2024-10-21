package service;

import dataaccess.DataAccess;

public class AuthService {
  private final DataAccess dataAccess;

  public AuthService(DataAccess dataAccess) {
    this.dataAccess=dataAccess;
  }
}
