package ui;

import chess.ChessGame;
import server.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
  private final ServerFacade serverFacade;
  private ClientInterface client;
  private String url;

  public Repl(String port) {
    url = "http://localhost:" + port;
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
        if (resultWords.length >= 7 && resultWords[6].equals("401")) {
          result = "Unauthorized, please try logging in again.";
        }
        else if (resultWords.length >= 2 && resultWords[1].equals("403")) {
          result = "User with this username already exists, please use a different username :)";
        }
        if (resultWords.length >= 4 && (resultWords[3].equals("joined") || resultWords[2].equals("observing"))) {
          System.out.println();
          String authToken = client.getAuthToken();
          String username = client.getUsername();
          if (resultWords.length >= 7 && resultWords[7].equals("WHITE")) {
            int gameIndex=Integer.parseInt(resultWords[5]);
            client=new GameClient(serverFacade, authToken, username, ChessGame.TeamColor.WHITE, gameIndex, url);
          }
          else if (resultWords.length >= 7 && resultWords[7].equals("BLACK")) {
            int gameIndex=Integer.parseInt(resultWords[5]);
            client=new GameClient(serverFacade, authToken, username, ChessGame.TeamColor.BLACK, gameIndex, url);
          }


          else {
            int gameIndex = Integer.parseInt(resultWords[4]);
            client = new GameClient(serverFacade, authToken, username, null, gameIndex, url);
          }


        }
        else if (resultWords.length >= 3 && resultWords[2].equals("left")) {
          String authToken = client.getAuthToken();
          String username = client.getUsername();
          client = new PostLoginClient(serverFacade, authToken, username);
          System.out.println(SET_TEXT_COLOR_BLUE);
          System.out.print(client.help());
        }
        else {
          System.out.print(SET_TEXT_COLOR_BLUE + SET_BG_COLOR_BLACK + result);
        }


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


public String getURL() {
    return url;
}
  private void printPrompt() {
    if (client instanceof GameClient) {
      try {
        Thread.sleep(250);
      }
      catch (Exception ex) {
        System.out.println("Time problem");
      }
    }
    System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN + SET_BG_COLOR_BLACK);
  }
}
