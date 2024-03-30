package chess.db.translator;

import chess.domain.square.piece.Color;
import chess.domain.square.piece.Piece;
import chess.domain.square.piece.unified.Bishop;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PieceTranslatorTest {

    @DisplayName("PieceType에 맞는 dbView를 찾는다.")
    @Test
    void translatePieceTypeToDbView() {
        String dbView = PieceTranslator.translate(Bishop.from(Color.BLACK));

        assertThat(dbView).isEqualTo("bishop");
    }

    @DisplayName("dbView와 color에 맞는 Piece를 만든다.")
    @Test
    void translateDbViewToPieceType() {
        Piece piece = PieceTranslator.translate("bishop", Color.WHITE);

        assertThat(piece).isEqualTo(Bishop.from(Color.WHITE));
    }
}