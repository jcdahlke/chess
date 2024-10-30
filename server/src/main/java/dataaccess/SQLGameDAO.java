package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDataAccess{
  public SQLGameDAO() throws DataAccessException {
    configureDatabase();
  }
  @Override
  public void clear() throws DataAccessException {
    String statement = "DELETE FROM game";
    executeUpdate(statement);
  }

  @Override
  public int createGame(String gameName) throws DataAccessException {
    var statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, json) VALUES (?, ?, ?, ?)";
    var json = new Gson().toJson(new ChessGame());
    return executeUpdate(statement, null, null, gameName, json);
  }

  @Override
  public GameData getGame(String gameID) throws DataAccessException {
    String query = "SELECT id, whiteUsername, blackUsername, gameName, json FROM game WHERE id = ?";

    try (var conn = DatabaseManager.getConnection();
         var ps = conn.prepareStatement(query)) {

      ps.setString(1, gameID);
      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          // Extract user data from the ResultSet
          String whiteUsername = rs.getString("whiteUsername");
          String blackUsername = rs.getString("blackUsername");
          String gameName = rs.getString("gameName");
          var json = rs.getString("json");
          var chessGame = new Gson().fromJson(json, ChessGame.class);
          // Return a new GameData object with the extracted data
          return new GameData(Integer.parseInt(gameID), whiteUsername, blackUsername, gameName, chessGame);
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error fetching game with ID: " + gameID + " - " + e.getMessage());
    }

    // If no user was found, return null or throw an exception depending on requirements
    return null;
  }

  @Override
  public Collection<GameData> listGames() throws DataAccessException {
    Collection<GameData> games = new ArrayList<>();
    String statement = "SELECT id, whiteUsername, blackUsername, gameName, json FROM game";
    try (var conn = DatabaseManager.getConnection();
         var ps = conn.prepareStatement(statement);
         var rs = ps.executeQuery()) {
          while (rs.next()) {
            int gameID = rs.getInt("id");
            String whiteUsername = rs.getString("whiteUsername");
            String blackUsername = rs.getString("blackUsername");
            String gameName = rs.getString("gameName");
            String json = rs.getString("json");

            // Parse the JSON data into a ChessGame object
            ChessGame chessGame = new Gson().fromJson(json, ChessGame.class);

            // Create and add the GameData object to the collection
            GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
            games.add(gameData);
          }
        }
    catch (Exception e) {
      throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
    }
    return games;
  }

  @Override
  public void updateGame(String gameID, String newUser, ChessGame.TeamColor color) throws DataAccessException {
    String statement;
    if (color == ChessGame.TeamColor.WHITE) {
      statement = "UPDATE game SET whiteUsername = ? WHERE id = ?";
    }
    else {
      statement = "UPDATE game SET blackUsername = ? WHERE id = ?";
    }
    try (var conn = DatabaseManager.getConnection();
         var ps = conn.prepareStatement(statement)) {
      // Set the new username and the game ID in the prepared statement
      ps.setString(1, newUser);
      ps.setString(2, gameID);

      // Execute the update
      int rowsAffected = ps.executeUpdate();

      // Optional: Check if the update was successful
      if (rowsAffected == 0) {
        throw new DataAccessException("No game found with ID: " + gameID);
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error updating game with ID: " + gameID + " - " + e.getMessage());
    }
  }

  @Override
  public int size() throws DataAccessException {
    String query = "SELECT COUNT(*) AS total FROM game";

    try (var conn = DatabaseManager.getConnection();
         var ps = conn.prepareStatement(query);
         var rs = ps.executeQuery()) {

      if (rs.next()) {
        return rs.getInt("total");  // Get the count from the "total" column
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error counting games in the table: " + e.getMessage());
    }

    return 0;
  }


  private int executeUpdate(String statement, Object... params) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
        for (var i = 0; i < params.length; i++) {
          var param = params[i];
          if (param instanceof String p) ps.setString(i + 1, p);
          else if (param instanceof Integer p) ps.setInt(i + 1, p);
          else if (param == null) ps.setNull(i + 1, NULL);
        }
        ps.executeUpdate();

        var rs = ps.getGeneratedKeys();
        if (rs.next()) {
          return rs.getInt(1);
        }

        return 0;
      }
    } catch (SQLException e) {
      throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
    }
  }

  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS  game (
              `id` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(`whiteUsername`),
              INDEX(`blackUsername`),
              INDEX(`gameName`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
  };


  private void configureDatabase() throws DataAccessException {
    DatabaseManager.createDatabase();
    try (var conn = DatabaseManager.getConnection()) {
      for (var statement : createStatements) {
        try (var preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.executeUpdate();
        }
      }
    } catch (SQLException ex) {
      throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
    }
  }
}
