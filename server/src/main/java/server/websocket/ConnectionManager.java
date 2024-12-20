package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  public final Map<Integer, Set<Session>> sessionMap = new HashMap<>();

  public void addSessionToGame(int gameID, Session session) {
    if (sessionMap.containsKey(gameID)) {
      Set<Session> sessions = sessionMap.get(gameID);
      sessions.add(session);
      sessionMap.replace(gameID, sessions);

    }
    else {
      Set<Session> set = new HashSet<Session>();
      set.add(session);
      sessionMap.put(gameID, set);
    }

  }

  public void removeSessionFromGame(int gameID, Session session) {
    sessionMap.get(gameID).remove(session);
  }


  public Set<Session> getSessionsForGame(int gameID) {
    return sessionMap.get(gameID);
  }


}
