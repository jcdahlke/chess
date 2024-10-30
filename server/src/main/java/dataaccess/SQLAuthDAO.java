package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO extends BaseDAOSQL implements AuthDataAccess {

  public SQLAuthDAO() {
    try {
      configureDatabase(createStatements);
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void clear() throws DataAccessException {
    String statement = "DELETE FROM auth";
    executeUpdate(statement);  // Use executeUpdate from BaseDAO
  }

  @Override
  public String createAuth(String username) throws DataAccessException {
    String authToken = generateToken();
    String statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
    executeUpdate(statement, username, authToken);  // Use executeUpdate from BaseDAO
    return authToken;
  }

  @Override
  public AuthData getAuth(String authToken) throws DataAccessException {
    String query = "SELECT username, authToken FROM auth WHERE authToken = ?";
    try (var conn = DatabaseManager.getConnection();
         var ps = conn.prepareStatement(query)) {

      ps.setString(1, authToken);  // Set the authToken parameter
      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          String username = rs.getString("username");
          return new AuthData(username, authToken);
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error fetching user with authToken: " + authToken + " - " + e.getMessage());
    }
    return null;  // Return null if no user is found
  }

  @Override
  public void deleteAuth(String authToken) throws DataAccessException {
    String statement = "DELETE FROM auth WHERE authToken = ?";
    executeUpdate(statement, authToken);  // Use executeUpdate from BaseDAO
  }

  @Override
  public int size() throws DataAccessException {
    String query = "SELECT COUNT(*) AS total FROM auth";
    try (var conn = DatabaseManager.getConnection();
         var ps = conn.prepareStatement(query);
         var rs = ps.executeQuery()) {

      if (rs.next()) {
        return rs.getInt("total");
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error counting auth tokens in the table: " + e.getMessage());
    }
    return 0;
  }

  public static String generateToken() {
    return UUID.randomUUID().toString();
  }

  private final String[] createStatements = {
          """
    CREATE TABLE IF NOT EXISTS auth (
      `username` varchar(256) NOT NULL,
      `authToken` varchar(256) NOT NULL,
      PRIMARY KEY (`authToken`),
      INDEX(`username`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
    """
  };

}
