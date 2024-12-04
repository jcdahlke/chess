package websocket.messages;

public class ErrorMessage extends ServerMessage {

  private String errorMessage = "Error: ";
  public ErrorMessage(String errorMessage) {
    super(ServerMessageType.ERROR);
    this.errorMessage += errorMessage;
  }
}
