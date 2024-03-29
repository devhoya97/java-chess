package chess.domain.square;

import chess.domain.square.piece.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ScoreTest {

    @DisplayName("점수가 높은 팀이 승리팀이다.")
    @Test
    void higherScoreIsWinner() {
        Score whiteScore = Score.of(1, Color.WHITE);
        Score blackScore = Score.of(2, Color.BLACK);

        Color winner = whiteScore.findWinner(blackScore);

        assertThat(winner).isEqualTo(Color.BLACK);
    }

    @DisplayName("점수가 같으면 무승부다.")
    @Test
    void NoColorIsWinner() {
        Score whiteScore = Score.of(1, Color.WHITE);
        Score blackScore = Score.of(1, Color.BLACK);

        Color winner = whiteScore.findWinner(blackScore);

        assertThat(winner).isEqualTo(Color.NO_COLOR);
    }
}