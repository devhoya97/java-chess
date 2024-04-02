package chess.db.dao;

import chess.db.DBConnector;
import chess.db.PieceInfo;
import chess.domain.position.File;
import chess.domain.position.Rank;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class PieceInfoDao {

    public void saveAll(Set<PieceInfo> pieceInfos) {
        Connection connection = DBConnector.getConnection();
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
        String query = "SELECT * FROM pieceInfo";
        Connection connection = DBConnector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return makePieceInfos(resultSet);
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<PieceInfo> makePieceInfos(ResultSet resultSet) throws SQLException {
        Set<PieceInfo> pieceInfos = new HashSet<>();
        while (resultSet.next()) {
            pieceInfos.add(makePieceInfo(resultSet));
        }
        return pieceInfos;
    }

    private PieceInfo makePieceInfo(ResultSet resultSet) throws SQLException {
        return new PieceInfo(
                resultSet.getString("color"),
                resultSet.getString("file"),
                resultSet.getString("rank"),
                resultSet.getString("pieceType")
        );
    }

    public void deleteAll() {
        String query = "DELETE FROM pieceInfo";
        Connection connection = DBConnector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(File file, Rank rank) {
        String query = "DELETE FROM pieceInfo WHERE file = ? AND `rank` = ?";
        Connection connection = DBConnector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, file.name());
            preparedStatement.setString(2, rank.name());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEmpty() {
        String query = "SELECT COUNT(*) FROM pieceInfo";
        Connection connection = DBConnector.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery()) {
            return size(resultSet) == 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int size(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }
}
