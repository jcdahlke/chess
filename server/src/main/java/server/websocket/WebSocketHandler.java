package server.websocket;

import com.google.gson.Gson;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

  private final ConnectionManager connections = new ConnectionManager();

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException {
    UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
    switch (action.getCommandType()) {
      case CONNECT -> enter(action.getAuthToken(), session);
      case LEAVE -> exit(action.getAuthToken());
    }
  }

  private void enter(String visitorName, Session session) throws IOException {
    connections.add(visitorName, session);
    var message = String.format("%s joined the game", visitorName);
    var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
    connections.broadcast(visitorName, notification);
  }

  private void exit(String visitorName) throws IOException {
    connections.remove(visitorName);
    var message = String.format("%s left the game", visitorName);
    var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
    connections.broadcast(visitorName, notification);
  }

}