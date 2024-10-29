package dataaccess;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTest {

  private final SQLUserDAO sqlUserDAO = new SQLUserDAO();
  private final UserDAO userDAO = new UserDAO();
  private final SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
  private final AuthDAO authDAO = new AuthDAO();
  private final SQLGameDAO sqlGameDAO = new SQLGameDAO();
  private final GameDAO gameDAO = new GameDAO();
  private final UserDataAccess userDataAccess = sqlUserDAO;
  private final AuthDataAccess authDataAccess = sqlAuthDAO;
  private final GameDataAccess gameDataAccess = sqlGameDAO;

  public DataAccessTest() throws DataAccessException {
  }

  @BeforeEach
  public void clearAll() throws DataAccessException {
    userDataAccess.clear();
    authDataAccess.clear();
    gameDataAccess.clear();
  }
@Test
void createUser() throws DataAccessException{
    userDataAccess.createUser("username", "password", "email");

    assertEquals(new UserData("username", "password", "email"), userDataAccess.getUser("username"));
}

@Test
void badCreateUser() throws DataAccessException {

}

@Test
void createAuth() throws DataAccessException {
    String authToken = authDataAccess.createAuth("username");

    assertEquals(new AuthData("username", authToken), authDataAccess.getAuth(authToken));
}

@Test
  void createGame() throws DataAccessException {
    int id =gameDataAccess.createGame("gameName");

    assertEquals(1, id);
}
}
