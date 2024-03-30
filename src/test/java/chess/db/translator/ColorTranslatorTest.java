package chess.db.translator;

import chess.domain.square.piece.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ColorTranslatorTest {

    @DisplayName("Color에 맞는 dbView를 찾는다.")
    @Test
    void translateColorToDbView() {
        String dbView = ColorTranslator.translate(Color.WHITE);

        assertThat(dbView).isEqualTo("white");
    }

    @DisplayName("dbView에 맞는 Color를 찾는다.")
    @Test
    void translateDbViewToColor() {
        Color color = ColorTranslator.translate("white");

        assertThat(color).isEqualTo(Color.WHITE);
    }
}