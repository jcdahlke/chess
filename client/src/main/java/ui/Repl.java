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
    System.out.println(SET_TEXT_COLOR_BLUE + SET_BG_COLOR_BLACK+"Welcome to Chess! Sign in to start.");
    System.out.print(client.help());
    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        result = client.eval(line);
        String[] resultWords = result.split(" ");
        if (resultWords.length >= 2 && resultWords[1].equals("401")) {
          result = "Unauthorized, please try logging in again.";
        }
        else if (resultWords.length >= 2 && resultWords[1].equals("403")) {
          result = "User with this username already exists, please use a different username :)";
        }
        System.out.print(SET_TEXT_COLOR_BLUE + SET_BG_COLOR_BLACK + result);

        if (resultWords.length > 1 && (resultWords[1].equals("signed") || resultWords[1].equals("registered"))) {
          String authToken = client.getAuthToken();
          String username = client.getUsername();
          client = new PostLoginClient(serverFacade, authToken, username);
          System.out.println();
          System.out.println();
          System.out.print(client.help());
        }
        if (resultWords.length >= 5 && resultWords[4].equals("out")) {
          client = new PreLoginClient(serverFacade);
          System.out.println();
          System.out.println();
          System.out.print(client.help());
        }


      } catch (Throwable e) {
        var msg = e.toString();

        if (msg.split(" ")[1].equals("401")) {
          msg = "Unauthorized, please try logging in again.";
        }
        else if (msg.split(" ")[1].equals("403")) {
          msg = "User with this username already exists, please use a different username :)";
        }
        System.out.print(msg);
      }
    }
    System.out.println();
  }



  private void printPrompt() {
    System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN + SET_BG_COLOR_BLACK);
  }
}
