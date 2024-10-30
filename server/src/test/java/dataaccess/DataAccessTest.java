package dataaccess;

import chess.ChessGame;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

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
void goodCreateUser() throws DataAccessException{
  userDataAccess.createUser("testUser", "testPass", "testEmail@example.com");
  UserData retrievedUser = userDataAccess.getUser("testUser");
  assertNotNull(retrievedUser);
  assertEquals("testUser", retrievedUser.username());
  assertTrue(sqlUserDAO.authenticateUser("testPass", retrievedUser));
}

@Test
void badCreateUser() throws DataAccessException {
  assertThrows(DataAccessException.class, () -> {
    userDataAccess.createUser(null, "password", "email@example.com");
  });
}

@Test
void goodUserClear() throws DataAccessException {
  userDataAccess.createUser("testUser", "testPass", "testEmail@example.com");
  assertEquals(1, userDataAccess.size());
  userDataAccess.clear();
  assertEquals(0, userDataAccess.size());
}

@Test
void badUserClear() throws DataAccessException {
  try {
    userDataAccess.clear();
  } catch (DataAccessException e) {
    fail("Clear should not throw an exception on an empty table");
  }
}

@Test
void goodGetUser() throws DataAccessException {
  userDataAccess.createUser("testUser", "testPass", "testEmail@example.com");
  UserData retrievedUser = userDataAccess.getUser("testUser");
  assertNotNull(retrievedUser);
  assertEquals("testUser", retrievedUser.username());
  assertEquals("testEmail@example.com", retrievedUser.email());
  }

  @Test
  void badGetUser() throws DataAccessException {
    assertNull(userDataAccess.getUser("nonexistentUser"));
  }

  @Test
  void goodAuthenticateUser() throws DataAccessException {
    userDataAccess.createUser("testUser", "testPass", "testEmail@example.com");
    UserData retrievedUser = userDataAccess.getUser("testUser");
    assertTrue(userDataAccess.authenticateUser("testPass", retrievedUser));
  }

  @Test
  void badAuthenticateUser() throws DataAccessException {
    userDataAccess.createUser("testUser", "testPass", "testEmail@example.com");
    UserData retrievedUser = userDataAccess.getUser("testUser");
    assertFalse(userDataAccess.authenticateUser("wrongPass", retrievedUser));
  }

  @Test
  void goodUserSize() throws DataAccessException {
    assertEquals(0, userDataAccess.size());
    userDataAccess.createUser("user1", "password1", "email1@example.com");
    userDataAccess.createUser("user2", "password2", "email2@example.com");
    assertEquals(2, userDataAccess.size());
  }

  @Test
  void badUserSize() throws DataAccessException {
    userDataAccess.clear();
    assertEquals(0, userDataAccess.size());
  }

  @Test
  void goodCreateAuth() throws DataAccessException {
    String username = "testUser";
    String authToken = authDataAccess.createAuth(username);
    AuthData retrievedAuth = authDataAccess.getAuth(authToken);

    assertNotNull(retrievedAuth);
    assertEquals(username, retrievedAuth.username());
    assertEquals(authToken, retrievedAuth.authToken());
  }

  @Test
  void badCreateAuth() {
    assertThrows(DataAccessException.class, () -> {
      authDataAccess.createAuth(null);
    });
  }

  @Test
  void goodGetAuth() throws DataAccessException {
    String username = "testUser";
    String authToken = authDataAccess.createAuth(username);
    AuthData retrievedAuth = authDataAccess.getAuth(authToken);

    assertNotNull(retrievedAuth);
    assertEquals(username, retrievedAuth.username());
    assertEquals(authToken, retrievedAuth.authToken());
  }

  @Test
  void badGetAuth() throws DataAccessException {
    assertNull(authDataAccess.getAuth("nonexistentToken"));
  }

  @Test
  void goodDeleteAuth() throws DataAccessException {
    String username = "testUser";
    String authToken = authDataAccess.createAuth(username);
    assertNotNull(authDataAccess.getAuth(authToken));

    authDataAccess.deleteAuth(authToken);
    assertNull(authDataAccess.getAuth(authToken));
  }

  @Test
  void badDeleteAuth() {
    assertDoesNotThrow(() -> authDataAccess.deleteAuth("nonexistentToken"));
  }

  @Test
  void goodAuthSize() throws DataAccessException {
    assertEquals(0, authDataAccess.size());
    authDataAccess.createAuth("user1");
    authDataAccess.createAuth("user2");
    assertEquals(2, authDataAccess.size());
  }

  @Test
  void badAuthSize() throws DataAccessException {
    authDataAccess.clear();
    assertEquals(0, authDataAccess.size());
  }

  @Test
  void goodCreateGame() throws DataAccessException {
    String gameName = "Test Game";
    int gameId = gameDataAccess.createGame(gameName);

    GameData gameData = gameDataAccess.getGame(String.valueOf(gameId));
    assertNotNull(gameData);
    assertEquals(gameName, gameData.gameName());
    assertNull(gameData.whiteUsername());
    assertNull(gameData.blackUsername());
    assertNotNull(gameData.game());
  }

  @Test
  void badCreateGame() {
    assertThrows(DataAccessException.class, () -> {
      gameDataAccess.createGame(null);
    });
  }

  @Test
  void goodGetGame() throws DataAccessException {
    String gameName = "Test Game";
    int gameId = gameDataAccess.createGame(gameName);

    GameData retrievedGame = gameDataAccess.getGame(String.valueOf(gameId));
    assertNotNull(retrievedGame);
    assertEquals(gameName, retrievedGame.gameName());
  }

  @Test
  void badGetGame() throws DataAccessException {
    assertNull(gameDataAccess.getGame("9999")); // Assuming 9999 does not exist
  }

  @Test
  void goodListGames() throws DataAccessException {
    gameDataAccess.createGame("Game 1");
    gameDataAccess.createGame("Game 2");

    Collection<GameData> games = gameDataAccess.listGames();
    assertEquals(2, games.size());
  }

  @Test
  void goodUpdateGameWhiteUser() throws DataAccessException {
    String gameName = "Test Game";
    int gameId = gameDataAccess.createGame(gameName);

    gameDataAccess.updateGame(String.valueOf(gameId), "WhitePlayer", ChessGame.TeamColor.WHITE);
    GameData updatedGame = gameDataAccess.getGame(String.valueOf(gameId));

    assertEquals("WhitePlayer", updatedGame.whiteUsername());
  }

  @Test
  void goodUpdateGameBlackUser() throws DataAccessException {
    String gameName = "Test Game";
    int gameId = gameDataAccess.createGame(gameName);

    gameDataAccess.updateGame(String.valueOf(gameId), "BlackPlayer", ChessGame.TeamColor.BLACK);
    GameData updatedGame = gameDataAccess.getGame(String.valueOf(gameId));

    assertEquals("BlackPlayer", updatedGame.blackUsername());
  }

  @Test
  void badUpdateGameWithNonExistentID() {
    assertThrows(DataAccessException.class, () -> {
      gameDataAccess.updateGame("9999", "SomeUser", ChessGame.TeamColor.WHITE);
    });
  }

  @Test
  void goodGameSize() throws DataAccessException {
    assertEquals(0, gameDataAccess.size());
    gameDataAccess.createGame("Game 1");
    assertEquals(1, gameDataAccess.size());
    gameDataAccess.createGame("Game 2");
    assertEquals(2, gameDataAccess.size());
  }

  @Test
  void badGameSize() throws DataAccessException {
    gameDataAccess.clear();
    assertEquals(0, gameDataAccess.size());
  }
}
