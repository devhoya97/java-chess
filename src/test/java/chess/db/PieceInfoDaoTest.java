package chess.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PieceInfoDaoTest {

    private static final String TEST_COLOR = "white";
    private static final String TEST_FILE = "a";
    private static final String TEST_RANK = "1";
    private static final String TEST_PIECE_TYPE = "bishop";

    private final PieceInfoDao pieceInfoDao = new PieceInfoDao();
    
    @AfterEach
    void tearDown() {
        pieceInfoDao.deleteAll();
    }

    @DisplayName("기물 정보를 DB에 저장한다.")
    @Test
    void addPieceInfo() {
        Set<PieceInfo> pieceInfos = Set.of(new PieceInfo(TEST_COLOR, TEST_FILE, TEST_RANK, TEST_PIECE_TYPE));
        pieceInfoDao.saveAll(pieceInfos);

        assertThat(pieceInfoDao.findAll()).isEqualTo(pieceInfos);
    }

    @DisplayName("DB에 저장된 기물 정보를 삭제한다.")
    @Test
    void delete() {
        Set<PieceInfo> pieceInfos = Set.of(new PieceInfo(TEST_COLOR, TEST_FILE, TEST_RANK, TEST_PIECE_TYPE));
        pieceInfoDao.saveAll(pieceInfos);

        pieceInfoDao.deleteAll();

        assertThat(pieceInfoDao.findAll()).isEmpty();
    }
}