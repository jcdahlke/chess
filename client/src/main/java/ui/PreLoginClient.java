package ui;

import server.ServerFacade;

import java.util.Arrays;

public class PreLoginClient {

  private final ServerFacade serverFacade;

  public PreLoginClient(ServerFacade server) {
    serverFacade = server;
  }

  public String eval(String input) {
    try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "login" -> login(params);
        case "register" -> register(params);
        case "quit" -> "quit";
        default -> help();
      };
    } catch (Exception ex) {
      return ex.getMessage();
    }
  }

  public String login(String... params) throws Exception {
    if (params.length == 2) {

      String username = params[0];
      String password = params[1];
      serverFacade.login(username, password);

      return String.format("You signed in as %s.", username);
    }
    throw new Exception("Expected: <username> <password>");
  }

  public String register(String... params) throws Exception {
    if (params.length == 3) {
      String username = params[0];
      String password = params[1];
      String email = params[2];
      serverFacade.register(username, password, email);

      return String.format("You registered as %s.", username);
    }
    throw new Exception("Expected: <username> <password> <email>");
  }

  public String help() {
    return"""
          - login <username> <password>
          - register <username> <password> <email>
          - help
          - quit
          """;
  }
}
