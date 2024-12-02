package websocket;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebsocketFacade extends Endpoint {

  Session session;


  public WebsocketFacade(String url) throws Exception {
    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/ws");

      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, socketURI);

      //set message handler
      this.session.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String message) {

        }
      });
    } catch (DeploymentException | IOException | URISyntaxException ex) {
      throw new Exception(ex.getMessage());
    }
  }

  //Endpoint requires this method, but you don't have to do anything
  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {
  }

  public void joinGame(String authToken, int gameID) throws Exception {
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

}

