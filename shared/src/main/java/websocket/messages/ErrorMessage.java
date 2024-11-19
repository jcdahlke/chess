package websocket.messages;

public class ErrorMessage extends ServerMessage {

  private String errorMessage = "Error: ";
  public ErrorMessage(ServerMessageType type, String errorMessage) {
    super(type);
    this.errorMessage += errorMessage;
  }
}
