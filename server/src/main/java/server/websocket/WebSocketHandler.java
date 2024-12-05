package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;

import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.Set;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

  private final ConnectionManager connections = new ConnectionManager();
  private final Service service;

  public WebSocketHandler(Service service) {
    this.service = service;
  }
  @OnWebSocketError
  public void onError(Throwable throwable) {
    System.out.println(throwable.toString());
  }
  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException, DataAccessException {
    UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
    String authToken = action.getAuthToken();
    String error = "";
    try {
      service.getUsername(authToken);
    }
    catch (NullPointerException ex) {
      error = "You are unauthorized";
      ErrorMessage errorMessage = new ErrorMessage(error);
      sendMessage(errorMessage, session);
    }
    int gameID = action.getGameID();

    try {
      service.getGame(gameID);
    }
    catch (Exception ex) {
      error = "There is no game with this gameID";
      ErrorMessage errorMessage = new ErrorMessage(error);
      sendMessage(errorMessage, session);
    }
    if (error.isEmpty()) {
      switch (action.getCommandType()) {
        case CONNECT -> connect(message, authToken, gameID, session);
        case MAKE_MOVE -> makeMove(message, authToken, gameID, session);
        case LEAVE -> leaveGame(message, authToken, gameID, session);
        case RESIGN -> resignGame(message, authToken, gameID, session);
      }
    }

  }

  //connect sends a LoadGameMessage to root client, and a Notification Message to all other clients
  private void connect(String message, String authToken, int gameID, Session session) throws IOException, DataAccessException {
//    connections.add(visitorName, session);
//    var message = String.format("%s joined the game", visitorName);
//    var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
//    broadcastMessage();
    connections.addSessionToGame(gameID, session);
    LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, service.getGame(gameID));
    String username = service.getUsername(authToken);
    String notification = username + " has connected to the game";
    NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notification);
    sendMessage(loadGameMessage, session);
    broadcastMessage(gameID, notificationMessage, session);


  }

  //makeMove sends a LOAD_GAME message to all clients, and a Notification Message to all other clients
  private void makeMove(String message, String authToken, int gameID, Session session) {
    LoadGameMessage loadGameMessage = null;

  }

  //leaveGame sends a Notification Message to all other clients
  private void leaveGame(String message, String authToken, int gameID, Session session) {

  }

  //resignGame sends a notification message to all
  private void resignGame(String message, String authToken, int gameID, Session session) {

  }
//  private void exit(String visitorName) throws IOException {
//    connections.remove(visitorName);
//    var message = String.format("%s left the game", visitorName);
//    var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
//    connections.broadcast(visitorName, notification);
//  }

  public void sendMessage(ServerMessage message, Session session) throws IOException {
    if (session.isOpen()) {
      session.getRemote().sendString(new Gson().toJson(message));
    }
  }

  public void broadcastMessage(int gameID, ServerMessage message, Session session) throws IOException {
    Set<Session> sessions = connections.getSessionsForGame(gameID);
    for (Session ses: sessions) {
      if (ses.isOpen() && !session.equals(ses)){
        ses.getRemote().sendString(new Gson().toJson(message));
      }


    }
  }

}