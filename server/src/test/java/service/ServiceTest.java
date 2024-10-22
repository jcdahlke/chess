package service;
import chess.ChessGame;
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

import java.util.ArrayList;
import java.util.Collection;

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

  @Test
  void goodCreateGame() throws DataAccessException {
    var authToken = authDAO.createAuth("username");

    assertNull(gameDAO.getGame("1"));

    String gameID = Integer.toString(gameService.createGame(authToken, "gameName"));
     assertNotNull(gameDAO.getGame(gameID));
  }

  @Test
  void badCreateGame() throws DataAccessException{
    authDAO.createAuth("username");

    Exception exception = assertThrows(DataAccessException.class, () -> {
      gameService.createGame("fake authToken", "gameName");
    });

    assertEquals("unauthorized", exception.getMessage());
  }

  @Test
  void goodListGames() throws DataAccessException {
    var authToken = authDAO.createAuth("username");
    String gameID1 = Integer.toString(gameDAO.createGame("game1"));
    String gameID2 = Integer.toString(gameDAO.createGame("game2"));
    String gameID3 = Integer.toString(gameDAO.createGame("game3"));
    Collection<GameData> expectedList = new ArrayList<>();
    expectedList.add(gameDAO.getGame(gameID1));
    expectedList.add(gameDAO.getGame(gameID2));
    expectedList.add(gameDAO.getGame(gameID3));

    var actualList = gameService.listGames(authToken);

    assertIterableEquals(expectedList, actualList);

  }

  @Test
  void badListGames() throws DataAccessException{
    authDAO.createAuth("username");

    Exception exception = assertThrows(DataAccessException.class, () -> {
      gameService.listGames("fake authToken");
    });

    assertEquals("unauthorized", exception.getMessage());
  }

  @Test
  void goodJoinGame() throws DataAccessException {
    var authToken = authDAO.createAuth("username");
    String gameID1 = Integer.toString(gameDAO.createGame("game1"));
    gameService.joinGame(authToken, gameID1, ChessGame.TeamColor.BLACK);
    var expectedBlackUsername = "username";
    var actualBlackUsername = gameDAO.getGame(gameID1).blackUsername();

    assertEquals(expectedBlackUsername, actualBlackUsername);
  }

  @Test
  void badBlackJoinGame() throws DataAccessException {
    var authToken1 = authDAO.createAuth("user1");
    var authToken2 = authDAO.createAuth("user2");
    String gameID1 = Integer.toString(gameDAO.createGame("game1"));
    gameService.joinGame(authToken1, gameID1, ChessGame.TeamColor.BLACK);

    Exception exception = assertThrows(DataAccessException.class, () -> {
      gameService.joinGame(authToken2, gameID1, ChessGame.TeamColor.BLACK);
    });

    assertEquals("Already Taken", exception.getMessage());
  }

  @Test
  void badWhiteJoinGame() throws DataAccessException {
    var authToken1 = authDAO.createAuth("user1");
    var authToken2 = authDAO.createAuth("user2");
    String gameID1 = Integer.toString(gameDAO.createGame("game1"));
    gameService.joinGame(authToken1, gameID1, ChessGame.TeamColor.WHITE);

    Exception exception = assertThrows(DataAccessException.class, () -> {
      gameService.joinGame(authToken2, gameID1, ChessGame.TeamColor.WHITE);
    });

    assertEquals("Already Taken", exception.getMessage());
  }
}
