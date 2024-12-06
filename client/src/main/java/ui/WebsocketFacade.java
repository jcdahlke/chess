package ui;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import server.ServerFacade;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebsocketFacade extends Endpoint {

  Session session;
  ServerFacade serverFacade;
  String playerColor;


  public WebsocketFacade(String url, String playerColor) throws Exception {
    if (playerColor == null) {
      this.playerColor = "";
    }
    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/ws");

      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, socketURI);

      //set message handler
      this.session.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String message) {
          try {
            // Deserialize the message into a base class or a message type
            ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);

            // Handle different types of messages based on their class or type
            if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
              LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
              handleLoadGameMessage(loadGameMessage);
            } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
              NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
              handleNotificationMessage(notificationMessage);
            } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
              ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
              handleErrorMessage(errorMessage);
            } else {
              System.out.println("Unknown message type received: " + message);
            }
          } catch (Exception e) {
            System.err.println("Failed to handle incoming message: " + e.getMessage());
          }
        }
      });
    } catch (DeploymentException | IOException | URISyntaxException ex) {
      throw new Exception(ex.getMessage());
    }
  }

  //Endpoint requires this method, but you don't have to do anything
  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {
    this.session = session;
    System.out.println("Connected to WebSocket server!");
  }

  private void handleLoadGameMessage(LoadGameMessage loadGameMessage) {
    ChessGame gameState = loadGameMessage.getGame();
    System.out.println();
    if (playerColor.equals("black")) {
      new DrawBoard(gameState.getBoard(), playerColor).displayBoard();
    }
    else {
      new DrawBoard(gameState.getBoard(), "white").displayBoard();
    }
    System.out.println();
  }

  private void handleNotificationMessage(NotificationMessage notificationMessage) {
    String notification = notificationMessage.getMessage();
    System.out.println(notification);
  }


  private void handleErrorMessage(ErrorMessage errorMessage) {
    String error = errorMessage.getErrorMessage();
    System.err.println(error);
  }


  public void joinGame(String authToken, int gameID, String color) throws Exception {
    try {
      var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(action));
    } catch (IOException ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public void leaveGame(String authToken, int gameID) throws Exception {
    try {
      var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(action));

      this.session.close();
    } catch (IOException ex) {
      throw new Exception(ex.getMessage());
    }
  }

  public void makeMove(String authToken, int gameID, ChessMove move) throws Exception {
    MakeMoveCommand moveCommand = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
    try {
      this.session.getBasicRemote().sendText(new Gson().toJson(moveCommand));
    } catch (IOException e) {
      throw new Exception(e.getMessage());
    }


  }

  public void resign(String authToken, int gameID) throws Exception {
    try {
      var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(action));

    } catch (IOException ex) {
      throw new Exception(ex.getMessage());
    }
  }

}

