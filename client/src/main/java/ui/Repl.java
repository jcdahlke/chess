package ui;

import server.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
  private final ServerFacade serverFacade;
  private ClientInterface client;

  public Repl(String port) {
    String url = "http://localhost:" + port + "/";
    serverFacade = new ServerFacade(url);
    client = new PreLoginClient(serverFacade);
  }

  public void run() {
    System.out.println("\uD83D\uDC36 Welcome to Chess! Sign in to start.");
    System.out.print(client.help());
    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        result = client.eval(line);
        System.out.print(SET_TEXT_COLOR_BLUE + result);
        if (result.split(" ")[1].equals("signed")) {
          String authToken = client.getAuthToken();
          client = new PostLoginClient(serverFacade, authToken);
        }
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
