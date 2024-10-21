package service;

import dataaccess.DataAccess;

public class ClearService {
  public ClearService(DataAccess dataAccess) {
    this.dataAccess=dataAccess;
  }

  private final DataAccess dataAccess;
  public void clearAllData() {
    dataAccess.clear();
  }
}
