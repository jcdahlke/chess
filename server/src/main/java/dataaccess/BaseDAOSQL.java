package dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static java.sql.Types.NULL;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public abstract class BaseDAOSQL {

  protected int executeUpdate(String statement, Object... params) throws DataAccessException {
    try (Connection conn = DatabaseManager.getConnection()) {
      try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
        for (int i = 0; i < params.length; i++) {
          var param = params[i];
          if (param instanceof String p) {
            ps.setString(i + 1, p);
          } else if (param instanceof Integer p) {
            ps.setInt(i + 1, p);
          } else if (param == null) {
            ps.setNull(i + 1, NULL);
          }
        }
        ps.executeUpdate();
        var rs = ps.getGeneratedKeys();
        return rs.next() ? rs.getInt(1) : 0;
      }
    } catch (SQLException e) {
      throw new DataAccessException(String.format("Unable to update database: %s, %s", statement, e.getMessage()));
    }
  }

  protected void configureDatabase(String[] createStatements) throws DataAccessException {
    DatabaseManager.createDatabase();
    try (Connection conn = DatabaseManager.getConnection()) {
      for (String statement : createStatements) {
        try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
          preparedStatement.executeUpdate();
        }
      }
    } catch (SQLException ex) {
      throw new DataAccessException("Unable to configure database: " + ex.getMessage());
    }
  }
}