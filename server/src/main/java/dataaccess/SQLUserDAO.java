package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.Objects;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDataAccess{
  public SQLUserDAO() {
    try {
      configureDatabase();
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }
  @Override
  public void clear() throws DataAccessException {
    String statement = "DELETE FROM user";
    executeUpdate(statement);
  }

  @Override
  public void createUser(String username, String password, String email) throws DataAccessException {
    var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    executeUpdate(statement, username, hashedPassword, email);
  }

  @Override
  public UserData getUser(String username) throws DataAccessException {
    String query = "SELECT username, password, email FROM user WHERE username = ?";

    try (var conn = DatabaseManager.getConnection();
         var ps = conn.prepareStatement(query)) {

      ps.setString(1, username); // Set the username parameter
      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          // Extract user data from the ResultSet
          String user = rs.getString("username");
          String password = rs.getString("password");
          String email = rs.getString("email");

          // Return a new UserData object with the extracted data
          return new UserData(user, password, email);
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error fetching user with username: " + username + " - " + e.getMessage());
    }

    // If no user was found, return null or throw an exception depending on requirements
    return null;
  }

  @Override
  public Boolean authenticateUser(String password, UserData user) throws DataAccessException {
    return BCrypt.checkpw(password, user.password());
  }

  @Override
  public int size() throws DataAccessException {
    String query = "SELECT COUNT(*) AS total FROM user";

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

  private int executeUpdate(String statement, Object... params) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
        for (var i = 0; i < params.length; i++) {
          var param = params[i];
          if (param instanceof String p){
            ps.setString(i + 1, p);
          }
          else if (param instanceof Integer p){
            ps.setInt(i + 1, p);
          }
          else if (param == null){
            ps.setNull(i + 1, NULL);
          }
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
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(`password`),
              INDEX(`email`)
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
