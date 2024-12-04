package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  public final Map<Integer, Set<Session>> sessionMap = new HashMap<>();

  public void addSessionToGame(int gameID, Session session) {
    sessionMap.get(gameID).add(session);
  }

  public void removeSessionFromGame(int gameID, Session session) {
    sessionMap.get(gameID).remove(session);
  }

  public void removeSession(Session session) {

  }

  public Set<Session> getSessionsForGame(int gameID) {
    return sessionMap.get(gameID);
  }


}
