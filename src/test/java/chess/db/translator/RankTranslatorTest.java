package chess.db.translator;

import chess.domain.position.Rank;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RankTranslatorTest {

    @DisplayName("rank에 맞는 dbView를 찾는다.")
    @Test
    void translateRankToDbView() {
        String dbView = RankTranslator.translate(Rank.FIRST);

        assertThat(dbView).isEqualTo("1");
    }

    @DisplayName("dbView에 맞는 rank을 찾는다.")
    @Test
    void translateDbViewToRank() {
        Rank rank = RankTranslator.translate("1");

        assertThat(rank).isEqualTo(Rank.FIRST);
    }
}