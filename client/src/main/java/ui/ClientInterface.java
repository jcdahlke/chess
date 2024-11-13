package ui;

public interface ClientInterface {
  public String eval(String input);

  public String help();

  public String getAuthToken();

  public String getUsername();
}
