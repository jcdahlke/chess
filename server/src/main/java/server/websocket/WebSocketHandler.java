package server.websocket;

import com.google.gson.Gson;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.Set;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

  private final ConnectionManager connections = new ConnectionManager();

  @OnWebSocketConnect
  public void onConnect(Session session) {

  }

  @OnWebSocketClose
  public void onClose(Session session) {

  }

  @OnWebSocketError
  public void onError(Throwable throwable) {

  }
  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException {
    UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
    switch (action.getCommandType()) {
      case CONNECT -> connect(new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message));
      case MAKE_MOVE -> makeMove();
      case LEAVE -> leaveGame();
      case RESIGN -> resignGame();
    }
  }

  //connect sends a LoadGameMessage to root client, and a Notification Message to all other clients
  private void connect(NotificationMessage message) throws IOException {
//    connections.add(visitorName, session);
//    var message = String.format("%s joined the game", visitorName);
//    var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
//    broadcastMessage();
  }

  //makeMove sends a LOAD_GAME message to all clients, and a Notification Message to all other clients
  private void makeMove() {

  }

  //leaveGame sends a Notification Message to all other clients
  private void leaveGame() {

  }

  private void resignGame() {

  }
//  private void exit(String visitorName) throws IOException {
//    connections.remove(visitorName);
//    var message = String.format("%s left the game", visitorName);
//    var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
//    connections.broadcast(visitorName, notification);
//  }

  public void sendMessage(ServerMessage message, Session session) {

  }

  public void broadcastMessage(int gameID, ServerMessage message, Session session) throws IOException {
    Set<Session> sessions = connections.getSessionsForGame(gameID);
    sessions.remove(session);
    for (Session ses: sessions) {
      if (ses.isOpen()){
        ses.getRemote().sendString(message.toString());
      }


    }
  }

}