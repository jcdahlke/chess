package client;

import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

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


    }

}
