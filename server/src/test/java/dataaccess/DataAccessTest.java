package dataaccess;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTest {

  private final SQLUserDAO sqlUserDAO = new SQLUserDAO();
  private final UserDAO userDAO = new UserDAO();
  private final UserDataAccess userDataAccess = sqlUserDAO;

  public DataAccessTest() throws DataAccessException {
  }

  @BeforeEach
  public void clearAll() throws DataAccessException {
    userDataAccess.clear();
  }
@Test
  public void createUser() throws DataAccessException{
    userDataAccess.createUser("username", "password", "email");

    assertEquals(new UserData("username", "password", "email"), userDataAccess.getUser("username"));
}
}
