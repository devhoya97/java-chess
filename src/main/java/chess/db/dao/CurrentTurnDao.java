package chess.db.dao;

import chess.db.DBConnector;
import chess.domain.CurrentTurn;
import chess.domain.square.piece.Color;

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
            deleteAll(connection);
            preparedStatement.setString(1, currentTurn.colorName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            return new CurrentTurn(readColor(resultSet));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Color readColor(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return Color.findByName(resultSet.getString("color"));
        }
        throw new IllegalStateException("저장된 CurrentTurn이 없습니다.");
    }
}
