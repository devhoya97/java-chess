package chess.db;

import chess.db.translator.ColorTranslator;
import chess.domain.CurrentTurn;

import java.sql.*;

public class CurrentTurnDao {
    private final DBConnector dbConnector;

    public CurrentTurnDao(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public void save(final CurrentTurn currentTurn) {
        String query = "INSERT INTO currentTurn VALUES(?)";
        Connection connection = dbConnector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            createTableIfNotExists(connection);
            deleteAll(connection);
            preparedStatement.setString(1, ColorTranslator.translate(currentTurn.value()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTableIfNotExists(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS currentTurn (color VARCHAR(10))");
        }
    }

    private void deleteAll(Connection connection) {
        String query = "DELETE FROM currentTurn";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAll() {
        String query = "DELETE FROM currentTurn";
        Connection connection = dbConnector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CurrentTurn find() {
        String  query = "SELECT * FROM currentTurn";
        Connection connection = dbConnector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new CurrentTurn(
                        ColorTranslator.translate(resultSet.getString("color"))
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
