package chess.domain.position;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PositionTest {

    @DisplayName("같은 File에 해당하는 Position들을 모두 찾는다.")
    @Test
    void findSameFilePositions() {
        Position position = new Position(Rank.FIRST, File.A);

        Set<Position> positions = position.sameFilePositions();

        assertThat(positions).containsExactlyInAnyOrder(
                new Position(Rank.SECOND, File.A), new Position(Rank.THIRD, File.A),
                new Position(Rank.FOURTH, File.A), new Position(Rank.FIFTH, File.A),
                new Position(Rank.SIXTH, File.A), new Position(Rank.SEVENTH, File.A),
                new Position(Rank.EIGHTH, File.A));
    }
}