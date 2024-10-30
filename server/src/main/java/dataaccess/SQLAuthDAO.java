package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAuthDAO implements AuthDataAccess{
  public SQLAuthDAO() {
    try {
      configureDatabase();
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }
  @Override
  public void clear() throws DataAccessException {
    String statement = "DELETE FROM auth";
    executeUpdate(statement);
  }

  @Override
  public String createAuth(String username) throws DataAccessException {
    String authToken = generateToken();
    var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
    executeUpdate(statement, username, authToken);
    return authToken;
  }

  @Override
  public AuthData getAuth(String authToken) throws DataAccessException {
    String query = "SELECT username, authToken FROM auth WHERE authToken = ?";

    try (var conn = DatabaseManager.getConnection();
         var ps = conn.prepareStatement(query)) {

      ps.setString(1, authToken); // Set the username parameter
      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          // Extract user data from the ResultSet
          String username = rs.getString("username");

          // Return a new UserData object with the extracted data
          return new AuthData(username, authToken);
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error fetching user with authToken: " + authToken + " - " + e.getMessage());
    }

    // If no user was found, return null or throw an exception depending on requirements
    return null;
  }


  @Override
  public void deleteAuth(String authToken) throws DataAccessException {
    var statement = "DELETE FROM auth WHERE authToken=?";
    executeUpdate(statement, authToken);
  }

  @Override
  public int size() throws DataAccessException {
    String query = "SELECT COUNT(*) AS total FROM auth";

    try (var conn = DatabaseManager.getConnection();
         var ps = conn.prepareStatement(query);
         var rs = ps.executeQuery()) {

      if (rs.next()) {
        return rs.getInt("total");  // Get the count from the "total" column
      }
    } catch (SQLException | DataAccessException e) {
      throw new DataAccessException("Error counting users in the table: " + e.getMessage());
    }

    return 0;
  }

  public static String generateToken() {
    return UUID.randomUUID().toString();
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
          CREATE TABLE IF NOT EXISTS auth (
          `username` varchar(256) NOT NULL,
          `authToken` varchar(256) NOT NULL,
          PRIMARY KEY (`authToken`),
          INDEX(`username`)
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

