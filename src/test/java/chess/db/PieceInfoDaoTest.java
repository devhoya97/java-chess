package chess.db;

import chess.db.dao.PieceInfoDao;
import chess.domain.position.File;
import chess.domain.position.Rank;
import chess.domain.square.piece.Color;
import chess.domain.square.piece.Piece;
import chess.domain.square.piece.unified.Bishop;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PieceInfoDaoTest {

    private static final Color TEST_COLOR = Color.WHITE;
    private static final File TEST_FILE = File.A;
    private static final Rank TEST_RANK = Rank.FIRST;
    private static final Piece TEST_PIECE = Bishop.from(TEST_COLOR);

    private final PieceInfoDao pieceInfoDao = new PieceInfoDao();
    
    @AfterEach
    void tearDown() {
        pieceInfoDao.delete(TEST_FILE, TEST_RANK);
    }

    @DisplayName("기물 정보를 DB에 저장한다.")
    @Test
    void addPieceInfo() {
        Set<PieceInfo> pieceInfos = Set.of(PieceInfo.of(TEST_COLOR, TEST_FILE, TEST_RANK, TEST_PIECE));
        pieceInfoDao.saveAll(pieceInfos);

        assertThat(pieceInfoDao.findAll()).isEqualTo(pieceInfos);
    }

    @DisplayName("DB에 저장된 기물 정보를 삭제한다.")
    @Test
    void delete() {
        Set<PieceInfo> pieceInfos = Set.of(PieceInfo.of(TEST_COLOR, TEST_FILE, TEST_RANK, TEST_PIECE));
        pieceInfoDao.saveAll(pieceInfos);

        pieceInfoDao.delete(TEST_FILE, TEST_RANK);

        assertThat(pieceInfoDao.findAll()).isEmpty();
    }
}