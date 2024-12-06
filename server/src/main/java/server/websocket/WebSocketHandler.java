package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.InvalidMoveException;
import com.google.gson.Gson;

import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.MakeMoveCommand;
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
  public void onMessage(Session session, String message) throws IOException, DataAccessException, InvalidMoveException {
    UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
    ChessMove chessMove = null;
    if (action.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
      MakeMoveCommand  makeMoveCommand= new Gson().fromJson(message, MakeMoveCommand.class);
      chessMove = makeMoveCommand.getMove();
    }
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
        case CONNECT -> connect(authToken, gameID, session);
        case MAKE_MOVE -> makeMove(authToken, gameID, session, chessMove);
        case LEAVE -> leaveGame(authToken, gameID, session);
        case RESIGN -> resignGame(authToken, gameID, session);
      }
    }

  }

  //connect sends a LoadGameMessage to root client, and a Notification Message to all other clients
  private void connect(String authToken, int gameID, Session session) throws IOException, DataAccessException {
    connections.addSessionToGame(gameID, session);
    LoadGameMessage loadGameMessage = new LoadGameMessage(service.getGame(gameID));
    String username = service.getUsername(authToken);
    ChessGame.TeamColor userColor = service.getUserColor(authToken, gameID);
    String color = "";
    String notification;
    if (userColor == ChessGame.TeamColor.WHITE) {
      color = "white";
    }
    else if (userColor == ChessGame.TeamColor.BLACK) {
      color = "black";
    }
    if (color.isEmpty()) {
      notification = username + " has connected to the game as an observer";
    }
    else {
      notification = username + " has connected to the game playing as " + color;
    }

    NotificationMessage notificationMessage = new NotificationMessage(notification);
    sendMessage(loadGameMessage, session);
    broadcastMessage(gameID, notificationMessage, session);
  }

  //makeMove sends a LOAD_GAME message to all clients, and a Notification Message to all other clients
  private void makeMove(String authToken, int gameID, Session session, ChessMove move) throws DataAccessException, InvalidMoveException, IOException {
    ChessGame game = service.getGame(gameID);
    String error;
    ChessPiece piece = game.getBoard().getPiece(move.getStartPosition());
    ChessGame.TeamColor userColor = service.getUserColor(authToken, gameID);
    if (userColor == null) {
      error = "observers cannot make moves";
      sendErrorMessage(error, session);
      return;
    }
    if (userColor != piece.getTeamColor()) {
      error = "Players cannot move pieces of the opposite color";
      sendErrorMessage(error, session);
      return;
    }
    if (game.getGameIsOver()) {
      game.isInCheckmate(userColor);
      error = "This game is already over";
      sendErrorMessage(error, session);
      return;
    }
    if(game.getTeamTurn() != userColor) {
      error = "It is not your turn";
      sendErrorMessage(error, session);
      return;
    }
    if (!game.validMoves(move.getStartPosition()).contains(move)) {
      error = "This move is invalid";
      sendErrorMessage(error, session);
      return;
    }
    service.makeMove(gameID, move);
    LoadGameMessage loadGameMessage = new LoadGameMessage(service.getGame(gameID));
    String username = service.getUsername(authToken);
    String start = move.getStartPosition().toString();
    String end = move.getEndPosition().toString();
    String notification = username + " moved " + start + " to " + end;
    NotificationMessage notificationMessage = new NotificationMessage(notification);
    sendMessage(loadGameMessage, session);
    broadcastMessage(gameID, loadGameMessage, session);
    broadcastMessage(gameID, notificationMessage, session);

    notification = checkGameCondition(authToken, gameID);
    if (!notification.isEmpty()) {
      notificationMessage = new NotificationMessage(notification);
      sendMessage(notificationMessage, session);
      broadcastMessage(gameID, notificationMessage, session);
    }

  }

  //leaveGame sends a Notification Message to all other clients
  private void leaveGame(String authToken, int gameID, Session session) throws DataAccessException, IOException {
    ChessGame.TeamColor userColor = service.getUserColor(authToken, gameID);
    if (userColor != null) {
      service.leaveGame(gameID, authToken);
    }
    connections.removeSessionFromGame(gameID, session);
    String username = service.getUsername(authToken);
    String endStatement = username + ", has left the game";
    String notification;
    if (userColor == null) {
      notification = "Observer, " + endStatement;
    }
    else if (userColor == ChessGame.TeamColor.WHITE) {
      notification = "Player white, " + endStatement;
    }
    else {
      notification = "Player black, " + endStatement;
    }
    NotificationMessage notificationMessage = new NotificationMessage(notification);
    broadcastMessage(gameID, notificationMessage, session);
  }

  //resignGame sends a notification message to all
  private void resignGame(String authToken, int gameID, Session session) throws DataAccessException, IOException {
    ChessGame.TeamColor userColor = service.getUserColor(authToken, gameID);
    ChessGame game = service.getGame(gameID);
    String error = "";
    if (userColor == null) {
      error = "observers cannot resign";
      sendErrorMessage(error, session);
      return;
    }
    if (game.getGameIsOver()) {
      error = "The game is already over, you cannot resign";
      sendErrorMessage(error, session);
      return;
    }
    service.resignGame(gameID, authToken);
    String username = service.getUsername(authToken);
    ChessGame.TeamColor winningColor = service.getGame(gameID).getWinningColor();
    String color;
    if (winningColor == ChessGame.TeamColor.WHITE) {
      color = "white";
    }
    else {
      color = "black";
    }
    String notification = username + " resigned from the game, meaning " + color + " has won the game!";
    NotificationMessage notificationMessage = new NotificationMessage(notification);
    sendMessage(notificationMessage, session);
    broadcastMessage(gameID, notificationMessage, session);
  }


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

  private void sendErrorMessage(String error, Session session) throws IOException {
    ErrorMessage errorMessage = new ErrorMessage(error);
    sendMessage(errorMessage, session);
  }

  private String checkGameCondition(String authToken, int gameID) throws DataAccessException {
    ChessGame.TeamColor userColor = service.getUserColor(authToken, gameID);
    ChessGame.TeamColor opponentColor;
    String message = "";
    String color;
    if (userColor == ChessGame.TeamColor.WHITE) {
      opponentColor = ChessGame.TeamColor.BLACK;
      color = "black";

    }
    else {
      opponentColor = ChessGame.TeamColor.WHITE;
      color = "white";
    }
    ChessGame game = service.getGame(gameID);

    if (game.isInCheck(opponentColor)) {
      message = color + " is in check";
    }
    if (game.isInCheckmate(opponentColor)) {
      message = color + " is in checkmate";

    }
    if (game.isInStalemate(opponentColor)) {
      message = "The game has ended in a stalemate";
    }
    return message;
  }

}