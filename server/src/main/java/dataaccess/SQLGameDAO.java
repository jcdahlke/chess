package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public class SQLGameDAO implements GameDataAccess{
  public SQLGameDAO() throws DataAccessException {
    configureDatabase();
  }
  @Override
  public void clear() {

  }

  @Override
  public int createGame(String gameName) throws DataAccessException {
    return 0;
  }

  @Override
  public GameData getGame(String gameID) throws DataAccessException {
    return null;
  }

  @Override
  public Collection<GameData> listGames() {
    return null;
  }

  @Override
  public void updateGame(String gameID, String newUser, ChessGame.TeamColor color) throws DataAccessException {

  }

  @Override
  public int size() {
    return 0;
  }


  private final String[] createStatements = {
          """
            CREATE TABLE IF NOT EXISTS  game (
              `id` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) NOT NULL,
              `blackUsername` varchar(256) NOT NULL,
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
