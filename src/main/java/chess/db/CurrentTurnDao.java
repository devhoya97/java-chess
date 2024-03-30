package chess.db;

import chess.db.translator.ColorTranslator;
import chess.domain.CurrentTurn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CurrentTurnDao {
    private static final String SERVER = "localhost:13306"; // MySQL 서버 주소
    private static final String DATABASE = "chess"; // MySQL DATABASE 이름
    private static final String OPTION = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USERNAME = "root"; //  MySQL 서버 아이디
    private static final String PASSWORD = "root"; // MySQL 서버 비밀번호

    public void save(final CurrentTurn currentTurn) {
        final var query = "INSERT INTO currentTurn VALUES(?)";
        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(query)) {
            deleteAll(connection);
            preparedStatement.setString(1, ColorTranslator.translate(currentTurn.value()));
            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + SERVER + "/" + DATABASE + OPTION, USERNAME, PASSWORD);
            createTableIfNotExists(connection);
            return connection;
        } catch (final SQLException e) {
            System.err.println("DB 연결 오류:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void createTableIfNotExists(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS currentTurn (color VARCHAR(10))");
        }
    }

    private void deleteAll(Connection connection) {
        final String query = "DELETE FROM currentTurn";
        try (var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAll() {
        final String query = "DELETE FROM currentTurn";
        try (Connection connection = getConnection();
             var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CurrentTurn find() {
        final var query = "SELECT * FROM currentTurn";
        try (final var connection = getConnection();
            final var preparedStatement = connection.prepareStatement(query)) {
            final var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new CurrentTurn(
                        ColorTranslator.translate(resultSet.getString("color"))
                );
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
