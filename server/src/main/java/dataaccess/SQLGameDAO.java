package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO extends BaseDAOSQL implements GameDataAccess {

  public SQLGameDAO() {
    try {
      configureDatabase(createStatements);
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void clear() throws DataAccessException {
    String statement = "DELETE FROM game";
    executeUpdate(statement);
  }

  @Override
  public int createGame(String gameName) throws DataAccessException {
    String statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, json) VALUES (?, ?, ?, ?)";
    String json = new Gson().toJson(new ChessGame());
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
          String whiteUsername = rs.getString("whiteUsername");
          String blackUsername = rs.getString("blackUsername");
          String gameName = rs.getString("gameName");
          String json = rs.getString("json");
          ChessGame chessGame = new Gson().fromJson(json, ChessGame.class);
          return new GameData(Integer.parseInt(gameID), whiteUsername, blackUsername, gameName, chessGame);
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error fetching game with ID: " + gameID + " - " + e.getMessage());
    }
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
        ChessGame chessGame = new Gson().fromJson(json, ChessGame.class);
        games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame));
      }
    } catch (SQLException e) {
      throw new DataAccessException("Unable to read data: " + e.getMessage());
    }
    return games;
  }

  @Override
  public void updateGame(String gameID, String newUser, ChessGame.TeamColor color) throws DataAccessException {
    String statement = color == ChessGame.TeamColor.WHITE
            ? "UPDATE game SET whiteUsername = ? WHERE id = ?"
            : "UPDATE game SET blackUsername = ? WHERE id = ?";
    try (var conn = DatabaseManager.getConnection();
         var ps = conn.prepareStatement(statement)) {
      ps.setString(1, newUser);
      ps.setString(2, gameID);
      if (ps.executeUpdate() == 0) {
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
      return rs.next() ? rs.getInt("total") : 0;
    } catch (SQLException e) {
      throw new DataAccessException("Error counting games in the table: " + e.getMessage());
    }
  }

  private final String[] createStatements = {
          """
      CREATE TABLE IF NOT EXISTS game (
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
}
