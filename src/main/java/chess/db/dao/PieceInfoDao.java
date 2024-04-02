package chess.db.dao;

import chess.db.DBConnector;
import chess.db.PieceInfo;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class PieceInfoDao {
    private final DBConnector dbConnector;

    public PieceInfoDao(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public void saveAll(Set<PieceInfo> pieceInfos) {
        Connection connection = dbConnector.getConnection();
        pieceInfos.forEach(pieceInfo -> save(pieceInfo, connection));
    }

    private void save(PieceInfo pieceInfo, Connection connection) {
        String query = "INSERT INTO pieceInfo VALUES(?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, pieceInfo.colorName());
            preparedStatement.setString(2, pieceInfo.fileName());
            preparedStatement.setString(3, pieceInfo.rankName());
            preparedStatement.setString(4, pieceInfo.pieceName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<PieceInfo> findAll() {
        Set<PieceInfo> pieceInfos = new HashSet<>();
        String query = "SELECT * FROM pieceInfo";
        Connection connection = dbConnector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
        Connection connection = dbConnector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEmpty() {
        String query = "SELECT COUNT(*) FROM pieceInfo";
        Connection connection = dbConnector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
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
