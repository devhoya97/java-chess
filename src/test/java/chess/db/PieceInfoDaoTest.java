package chess.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class PieceInfoDaoTest {

    private static final String TEST_COLOR = "white";
    private static final String TEST_COLOR_FOR_UPDATE = "white";
    private static final String TEST_FILE = "a";
    private static final String TEST_RANK = "1";
    private static final String TEST_PIECE_TYPE = "bishop";
    private static final String TEST_PIECE_TYPE_FOR_UPDATE = "bishop";
    
    private final PieceInfoDao pieceInfoDao = new PieceInfoDao();
    
    @AfterEach
    void tearDown() {
        pieceInfoDao.delete(TEST_FILE, TEST_RANK);
    }
    
    @Test
    void connection() throws SQLException {
        try (Connection connection = pieceInfoDao.getConnection()) {
            assertThat(connection).isNotNull();
        }
    }

    @Test
    void addPieceInfo() {
        PieceInfo pieceInfo = new PieceInfo(TEST_COLOR, TEST_FILE, TEST_RANK, TEST_PIECE_TYPE);
        pieceInfoDao.add(pieceInfo);

        assertThat(pieceInfoDao.findByFileRank(TEST_FILE, TEST_RANK)).isEqualTo(pieceInfo);
        pieceInfoDao.delete(TEST_FILE, TEST_RANK);
    }

    @Test
    void findByFileRank() {
        PieceInfo pieceInfo = new PieceInfo(TEST_COLOR, TEST_FILE, TEST_RANK, TEST_PIECE_TYPE);
        pieceInfoDao.add(pieceInfo);
        PieceInfo actual = pieceInfoDao.findByFileRank(TEST_FILE, TEST_RANK);

        assertThat(actual).isEqualTo(pieceInfo);
        pieceInfoDao.delete(TEST_FILE, TEST_RANK);
    }

    @Test
    void update() {
        PieceInfo initialPieceInfo = new PieceInfo(TEST_COLOR, TEST_FILE, TEST_RANK, TEST_PIECE_TYPE);
        pieceInfoDao.add(initialPieceInfo);
        PieceInfo updatedPieceInfo = new PieceInfo(TEST_COLOR_FOR_UPDATE, TEST_FILE, TEST_RANK, TEST_PIECE_TYPE_FOR_UPDATE);
        pieceInfoDao.update(updatedPieceInfo);
        PieceInfo actual = pieceInfoDao.findByFileRank(TEST_FILE, TEST_RANK);

        assertThat(actual).isEqualTo(updatedPieceInfo);
        pieceInfoDao.delete(TEST_FILE, TEST_RANK);
    }

    @Test
    void delete() {
        PieceInfo pieceInfo = new PieceInfo(TEST_COLOR, TEST_FILE, TEST_RANK, TEST_PIECE_TYPE);
        pieceInfoDao.add(pieceInfo);

        pieceInfoDao.delete(TEST_FILE, TEST_RANK);

        assertThat(pieceInfoDao.findByFileRank(TEST_FILE, TEST_RANK)).isNull();
    }
}