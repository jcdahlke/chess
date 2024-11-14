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
    System.out.println("Welcome to Chess! Sign in to start.");
    System.out.print(client.help());
    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        result = client.eval(line);
        System.out.print(SET_TEXT_COLOR_BLUE + result);
        String[] resultWords = result.split(" ");
        if (resultWords.length > 1 && resultWords[1].equals("signed")) {
          String authToken = client.getAuthToken();
          String username = client.getUsername();
          client = new PostLoginClient(serverFacade, authToken, username);
          System.out.print(client.help());
        }
        if (resultWords.length >= 5 && resultWords[4].equals("out")) {
          client = new PreLoginClient(serverFacade);
          System.out.println();
          System.out.print(client.help());
        }
      } catch (Throwable e) {
        var msg = e.toString();
        System.out.print(msg);
      }
    }
    System.out.println();
  }



  private void printPrompt() {
    System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN);
  }
}
