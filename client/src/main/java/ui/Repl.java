package ui;

import server.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
  private final PreLoginClient preLoginClient;
  private final PostLoginClient postLoginClient;
  private final GameClient gameClient;
  private final ServerFacade serverFacade;

  public Repl(String port) {
    String url = "http://localhost:" + port + "/";
    serverFacade = new ServerFacade(url);
    preLoginClient = new PreLoginClient(serverFacade);
    postLoginClient = new PostLoginClient(serverFacade);
    gameClient = new GameClient(serverFacade);
  }

  public void run() {
    System.out.println("\uD83D\uDC36 Welcome to the pet store. Sign in to start.");
    System.out.print(preLoginClient.help());

    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        result = preLoginClient.eval(line);
        System.out.print(SET_TEXT_COLOR_BLUE + result);
      } catch (Throwable e) {
        var msg = e.toString();
        System.out.print(msg);
      }
    }
    System.out.println();
  }



  private void printPrompt() {
    System.out.print("\n" + "\u001b0m" + ">>> " + SET_TEXT_COLOR_GREEN);
  }
}
