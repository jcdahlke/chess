package websocket;

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


  public WebsocketFacade(String url, ServerFacade serverFacade) throws Exception {
    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/ws");
      this.serverFacade = serverFacade;

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
            if (serverMessage instanceof LoadGameMessage) {
              LoadGameMessage loadGameMessage = (LoadGameMessage) serverMessage;
              handleLoadGameMessage(loadGameMessage);
            } else if (serverMessage instanceof NotificationMessage) {
              NotificationMessage notificationMessage = (NotificationMessage) serverMessage;
              handleNotificationMessage(notificationMessage);
            } else if (serverMessage instanceof ErrorMessage) {
              ErrorMessage errorMessage = (ErrorMessage) serverMessage;
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
    // Example: Update the game state in the client
    ChessGame gameState = loadGameMessage.getGame();
    System.out.println("Game updated: " + gameState);
    // Update your UI or internal game state representation
  }

  private void handleNotificationMessage(NotificationMessage notificationMessage) {
    String notification = notificationMessage.getMessage();
    System.out.println(notification);
    // Display this notification to the user
  }


  private void handleErrorMessage(ErrorMessage errorMessage) {
    String error = errorMessage.getErrorMessage();
    System.err.println(error);
    // Show an error message to the user
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

  public void makeMove(String authToken, int gameID, ChessMove move) {
    MakeMoveCommand moveCommand = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);


  }

}

