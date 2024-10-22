package service;
import service.UserService;
import service.GameService;
import service.ClearService;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
  private final AuthDAO authDAO= new AuthDAO();
  private final GameDAO gameDAO= new GameDAO();
  private final UserDAO userDAO= new UserDAO();
  private final ClearService clearService = new ClearService(gameDAO, userDAO, authDAO);
  private final GameService gameService = new GameService(gameDAO, authDAO);
  private final UserService userService = new UserService(userDAO, authDAO);

  @BeforeEach
  void clear() {
    clearService.clearAllData();
  }

  @Test
  void register() throws DataAccessException {
    var expected = new AuthData("username", "authToken");
    var registerReturn = userService.register("username", "password", "email");

    assertEquals(expected.username(), registerReturn.username());

    var expectedUser = new UserData("username", "password", "email");
    var actualUser = userDAO.getUser("username");
    assertEquals(expectedUser, actualUser);
  }

  @Test
  void badReRegister() throws DataAccessException {
    userService.register("username", "password", "email");

    Exception exception = assertThrows(DataAccessException.class, () -> {
      userService.register("username", "password", "email");
    });

    assertEquals("User already exists", exception.getMessage());
  }

  @Test
  void goodLogIn() throws DataAccessException  {
    userDAO.createUser("username", "password", "email");
    var actualAuthData = userService.userLogin("username", "password");
    var expectedAuthData = authDAO.getAuth(actualAuthData.authToken());

    assertEquals(expectedAuthData, actualAuthData);
  }

  @Test
  void badUsernameLogIn() throws DataAccessException  {
    userDAO.createUser("username", "password", "email");
    Exception exception = assertThrows(DataAccessException.class, () -> {
      userService.userLogin("bad username", "password");
    });

    assertEquals("Invalid username", exception.getMessage());
  }

  @Test
  void badPasswordLogIn() throws DataAccessException  {
    userDAO.createUser("username", "password", "email");
    Exception exception = assertThrows(DataAccessException.class, () -> {
      userService.userLogin("username", "bad password");
    });

    assertEquals("Wrong password", exception.getMessage());
  }

  @Test
  void goodLogOut() throws DataAccessException  {
    var authToken = authDAO.createAuth("username");
    userService.logout(authToken);
    var actualAuthData = authDAO.getAuth(authToken);
    assertNull(actualAuthData);
  }

  @Test
  void badLogOut() throws DataAccessException  {
    authDAO.createAuth("username");

    Exception exception = assertThrows(DataAccessException.class, () -> {
      userService.logout("fake authToken");
    });

    assertEquals("unauthorized", exception.getMessage());
  }


}
