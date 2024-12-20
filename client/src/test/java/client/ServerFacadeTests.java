package client;

import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port + "/");
    }

    @BeforeEach
    public void clearAll() throws Exception {
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerGood() throws Exception {
        var authData = serverFacade.register("player1", "password", "p1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void registerBad() throws Exception {
        serverFacade.register("player1", "password", "p1@email.com");
        assertThrows(Exception.class, () -> {
            serverFacade.register("player1", "password", "p1@email.com");
        });
    }

    @Test
    public void clearGood() throws Exception {
        serverFacade.register("player1", "password", "p1@email.com");
        serverFacade.clear();
        var authData = serverFacade.register("player1", "password", "p1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void clearBad() throws Exception {
        serverFacade.register("player1", "password", "p1@email.com");
        serverFacade.clear();
        assertThrows(Exception.class, () -> {
            serverFacade.login("player1", "password");
        });
    }

    @Test
    public void loginGood() throws Exception {
        serverFacade.register("player1", "password", "p1@email.com");
        var authData = serverFacade.login("player1", "password");
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void loginBadUsername() throws Exception {
        serverFacade.register("player1", "password", "p1@email.com");
        assertThrows(Exception.class, () -> {
            serverFacade.login("player2", "password");
        });
    }

    @Test
    public void loginBadPassword() throws Exception {
        serverFacade.register("player1", "password", "p1@email.com");
        assertThrows(Exception.class, () -> {
            serverFacade.login("player1", "PaSsWoRd");
        });
    }

    @Test
    public void logoutGood() throws Exception {
        serverFacade.register("username", "password", "email@gmail.com");
        AuthData authData = serverFacade.login("username", "password");
        serverFacade.logout(authData.authToken());
        assertThrows(Exception.class, () -> {
            serverFacade.logout(authData.authToken());
        });
    }

    @Test
    public void logoutBad() throws Exception {
        assertThrows(Exception.class, () -> {
            serverFacade.logout("FalseAuthToken");
        });
    }

    @Test
    public void createGameGood() throws Exception {
        AuthData authData = serverFacade.register("username", "password", "email");
        String authToken = authData.authToken();
        int gameID = serverFacade.createGame(authToken, "Joey'sGame");
        assertTrue(gameID > 0);
    }

    @Test
    public void createGameBad() throws Exception {
        assertThrows(Exception.class, () -> {
            serverFacade.createGame("FalseAuthToken", "gameName");
        });
    }

    @Test
    public void listGamesGood() throws Exception {
        AuthData authData = serverFacade.register("username", "password", "email");
        String authToken = authData.authToken();
        serverFacade.createGame(authToken, "gameEX1");
        serverFacade.createGame(authToken, "gameEX2");
        serverFacade.createGame(authToken, "gameEX3");
        serverFacade.createGame(authToken, "gameEX4");
        Collection<GameData> games = serverFacade.listGames(authToken);
        assertEquals(4, games.size());
    }

    @Test
    public void listGamesBad() throws Exception {
        AuthData authData = serverFacade.register("username", "password", "email");
        String authToken = authData.authToken();
        serverFacade.createGame(authToken, "gameEX1");
        assertThrows(Exception.class, () -> {
            serverFacade.listGames("FalseAuthToken");
        });
    }

    @Test
    public void joinGameGood() throws Exception {
        AuthData authData = serverFacade.register("username", "password", "email");
        String authToken = authData.authToken();
        int gameID = serverFacade.createGame(authToken, "game1");
        serverFacade.joinGame(authToken, String.valueOf(gameID), "BLACK");
        Collection<GameData> games = serverFacade.listGames(authToken);
        GameData firstGame = games.iterator().next();
        assertEquals("username", firstGame.blackUsername());
    }

    @Test
    public void joinGameBad() throws Exception {
        AuthData authData = serverFacade.register("username", "password", "email");
        String authToken = authData.authToken();
        int gameID = serverFacade.createGame(authToken, "game1");
        assertThrows(Exception.class, () -> {
            serverFacade.joinGame(authToken, String.valueOf(gameID), "Orange");
        });

    }
}
