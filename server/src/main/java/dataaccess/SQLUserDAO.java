package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class SQLUserDAO extends BaseDAOSQL implements UserDataAccess {

  public SQLUserDAO() {
    try {
      configureDatabase(createStatements);
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
    String statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    executeUpdate(statement, username, hashedPassword, email);
  }

  @Override
  public UserData getUser(String username) throws DataAccessException {
    String query = "SELECT username, password, email FROM user WHERE username = ?";
    try (var conn = DatabaseManager.getConnection();
         var ps = conn.prepareStatement(query)) {
      ps.setString(1, username);
      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
        }
      }
    } catch (SQLException e) {
      throw new DataAccessException("Error fetching user with username: " + username + " - " + e.getMessage());
    }
    return null;
  }

  @Override
  public Boolean authenticateUser(String password, UserData user) {
    return BCrypt.checkpw(password, user.password());
  }

  @Override
  public int size() throws DataAccessException {
    String query = "SELECT COUNT(*) AS total FROM user";
    try (var conn = DatabaseManager.getConnection();
         var ps = conn.prepareStatement(query);
         var rs = ps.executeQuery()) {
      return rs.next() ? rs.getInt("total") : 0;
    } catch (SQLException e) {
      throw new DataAccessException("Error counting users in the table: " + e.getMessage());
    }
  }

  private final String[] createStatements = {
          """
      CREATE TABLE IF NOT EXISTS user (
        `username` varchar(256) NOT NULL,
        `password` varchar(256) NOT NULL,
        `email` varchar(256) NOT NULL,
        PRIMARY KEY (`username`),
        INDEX(`password`),
        INDEX(`email`)
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
      """
  };
}
