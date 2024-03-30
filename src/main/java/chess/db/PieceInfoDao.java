package chess.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PieceInfoDao {
    private static final String SERVER = "localhost:13306"; // MySQL 서버 주소
    private static final String DATABASE = "chess"; // MySQL DATABASE 이름
    private static final String OPTION = "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USERNAME = "root"; //  MySQL 서버 아이디
    private static final String PASSWORD = "root"; // MySQL 서버 비밀번호

    public Connection getConnection() {
        // 드라이버 연결
        try {
            return DriverManager.getConnection("jdbc:mysql://" + SERVER + "/" + DATABASE + OPTION, USERNAME, PASSWORD);
        } catch (final SQLException e) {
            System.err.println("DB 연결 오류:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void add(final PieceInfo pieceInfo) {
        final var query = "INSERT INTO pieceInfo VALUES(?, ?, ?, ?)";
        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, pieceInfo.color());
            preparedStatement.setString(2, pieceInfo.file());
            preparedStatement.setString(3, pieceInfo.rank());
            preparedStatement.setString(4, pieceInfo.pieceType());
            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PieceInfo findByFileRank(final String file, String rank) {
        final var query = "SELECT * FROM pieceInfo WHERE file = ? AND `rank` = ?";
        try (final var connection = getConnection();
             final var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, file);
            preparedStatement.setString(2, rank);

            final var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new PieceInfo(
                        resultSet.getString("color"),
                        resultSet.getString("file"),
                        resultSet.getString("rank"),
                        resultSet.getString("pieceType")
                );
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void update(final PieceInfo pieceInfo) {
        final String query = "UPDATE pieceInfo SET color = ?, pieceType = ? WHERE file = ? AND `rank` = ?";
        try (Connection connection = getConnection();
            var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, pieceInfo.color());
            preparedStatement.setString(2, pieceInfo.pieceType());
            preparedStatement.setString(3, pieceInfo.file());
            preparedStatement.setString(4, pieceInfo.rank());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(final String file, final String rank) {
        final String query = "DELETE FROM pieceInfo WHERE file = ? AND `rank` = ?";
        try (Connection connection = getConnection();
            var preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, file);
            preparedStatement.setString(2, rank);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
