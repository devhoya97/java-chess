package chess.db;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class PieceInfoDao {
    private static final String SERVER = "localhost:13306"; // MySQL 서버 주소
    private static final String DATABASE = "chess"; // MySQL DATABASE 이름
    private static final String OPTION = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USERNAME = "root"; //  MySQL 서버 아이디
    private static final String PASSWORD = "root"; // MySQL 서버 비밀번호

    public void saveAll(final Set<PieceInfo> pieceInfos) {
        try (final var connection = getConnection()) {
            pieceInfos.forEach(pieceInfo -> save(pieceInfo, connection));
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

    private void save(PieceInfo pieceInfo, Connection connection) {
        final var query = "INSERT INTO pieceInfo VALUES(?, ?, ?, ?)";
        try (final var preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, pieceInfo.color());
            preparedStatement.setString(2, pieceInfo.file());
            preparedStatement.setString(3, pieceInfo.rank());
            preparedStatement.setString(4, pieceInfo.pieceType());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTableIfNotExists(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS pieceInfo (" +
                    "color VARCHAR(10) NOT NULL, " +
                    "file VARCHAR(10) NOT NULL, " +
                    "`rank` VARCHAR(10) NOT NULL, " +
                    "pieceType VARCHAR(10) NOT NULL, " +
                    "PRIMARY KEY (file, `rank`)" +
                    ")";
            statement.executeUpdate(createTableQuery);
        }
    }

    public Set<PieceInfo> findAll() {
        Set<PieceInfo> pieceInfos = new HashSet<>();
        final var query = "SELECT * FROM pieceInfo";
        try (final var connection = getConnection();
            final var preparedStatement = connection.prepareStatement(query)) {
            final var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                pieceInfos.add(new PieceInfo(
                        resultSet.getString("color"),
                        resultSet.getString("file"),
                        resultSet.getString("rank"),
                        resultSet.getString("pieceType")));
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
        return pieceInfos;
    }

    public void deleteAll() {
        final String query = "DELETE FROM pieceInfo";
        try (Connection connection = getConnection();
             var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEmpty() {
        final String query = "SELECT COUNT(*) FROM pieceInfo";
        try (Connection connection = getConnection();
            var preparedStatement = connection.prepareStatement(query);
            var resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
