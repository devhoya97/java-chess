package chess.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("DB와 연결한다.")
    @Test
    void connection() throws SQLException {
        try (Connection connection = pieceInfoDao.getConnection()) {
            assertThat(connection).isNotNull();
        }
    }

    @DisplayName("기물 정보를 DB에 저장한다.")
    @Test
    void addPieceInfo() {
        PieceInfo pieceInfo = new PieceInfo(TEST_COLOR, TEST_FILE, TEST_RANK, TEST_PIECE_TYPE);
        pieceInfoDao.add(pieceInfo);

        assertThat(pieceInfoDao.findByFileRank(TEST_FILE, TEST_RANK)).isEqualTo(pieceInfo);
    }

    @DisplayName("file과 rank 값을 key로 DB에서 기물 정보를 찾는다.")
    @Test
    void findByFileRank() {
        PieceInfo pieceInfo = new PieceInfo(TEST_COLOR, TEST_FILE, TEST_RANK, TEST_PIECE_TYPE);
        pieceInfoDao.add(pieceInfo);
        PieceInfo actual = pieceInfoDao.findByFileRank(TEST_FILE, TEST_RANK);

        assertThat(actual).isEqualTo(pieceInfo);
    }

    @DisplayName("DB에 저장된 기물 정보를 변경한다.")
    @Test
    void update() {
        PieceInfo initialPieceInfo = new PieceInfo(TEST_COLOR, TEST_FILE, TEST_RANK, TEST_PIECE_TYPE);
        pieceInfoDao.add(initialPieceInfo);
        PieceInfo updatedPieceInfo = new PieceInfo(TEST_COLOR_FOR_UPDATE, TEST_FILE, TEST_RANK, TEST_PIECE_TYPE_FOR_UPDATE);
        pieceInfoDao.update(updatedPieceInfo);
        PieceInfo actual = pieceInfoDao.findByFileRank(TEST_FILE, TEST_RANK);

        assertThat(actual).isEqualTo(updatedPieceInfo);
    }

    @DisplayName("DB에 저장된 기물 정보를 삭제한다.")
    @Test
    void delete() {
        PieceInfo pieceInfo = new PieceInfo(TEST_COLOR, TEST_FILE, TEST_RANK, TEST_PIECE_TYPE);
        pieceInfoDao.add(pieceInfo);

        pieceInfoDao.delete(TEST_FILE, TEST_RANK);

        assertThat(pieceInfoDao.findByFileRank(TEST_FILE, TEST_RANK)).isNull();
    }
}