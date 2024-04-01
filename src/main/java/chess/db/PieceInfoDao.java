package chess.db;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class PieceInfoDao {
    private final DBConnector dbConnector;

    public PieceInfoDao(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public void saveAll(Set<PieceInfo> pieceInfos) {
        try (Connection connection = dbConnector.getConnection()) {
            pieceInfos.forEach(pieceInfo -> save(pieceInfo, connection));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void save(PieceInfo pieceInfo, Connection connection) {
        String query = "INSERT INTO pieceInfo VALUES(?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            createTableIfNotExists(connection);
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
        String query = "SELECT * FROM pieceInfo";
        try (Connection connection = dbConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
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
        String query = "DELETE FROM pieceInfo";
        try (Connection connection = dbConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEmpty() {
        String query = "SELECT COUNT(*) FROM pieceInfo";
        try (Connection connection = dbConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery()) {
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
