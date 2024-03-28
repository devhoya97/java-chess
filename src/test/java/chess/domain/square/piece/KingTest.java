package chess.domain.square.piece;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.EmptySquaresMaker;
import chess.domain.position.File;
import chess.domain.position.Path;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.domain.square.Square;
import chess.domain.square.piece.unified.King;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class KingTest {

    @DisplayName("킹은 한 칸 짜리 경로이면 움직일 수 있다.")
    @ParameterizedTest
    @CsvSource({
            "FIRST, A, SECOND, A",
            "FIRST, A, SECOND, B",
    })
    void canMoveOneStep(Rank startRank, File startFile, Rank targetRank, File targetFile) {
        final Map<Position, Square> board = EmptySquaresMaker.make();
        Piece piece = King.from(Color.WHITE);
        board.put(new Position(startRank, startFile), piece);
        Path path = new Path(
                new Position(startRank, startFile), new Position(targetRank, targetFile));

        final boolean canMove = piece.canArrive(path, board);

        assertThat(canMove).isTrue();
    }

    @DisplayName("킹은 한 칸 짜리 경로가 아니면 움직일 수 없다.")
    @ParameterizedTest
    @CsvSource({
            "FIRST, A, THIRD, A",
            "FIRST, A, THIRD, C"
    })
    void canNotMoveUnlessOneStep(Rank startRank, File startFile, Rank targetRank, File targetFile) {
        final Map<Position, Square> board = EmptySquaresMaker.make();
        Piece piece = King.from(Color.WHITE);
        board.put(new Position(startRank, startFile), piece);
        Path path = new Path(
                new Position(startRank, startFile), new Position(targetRank, targetFile));

        final boolean canMove = piece.canArrive(path, board);

        assertThat(canMove).isFalse();
    }

    @DisplayName("킹은 한 칸 짜리 경로이면 공격할 수 있다.")
    @ParameterizedTest
    @CsvSource({
            "FIRST, A, SECOND, A",
            "FIRST, A, SECOND, B",
    })
    void canAttackOneStep(Rank startRank, File startFile, Rank targetRank, File targetFile) {
        final Map<Position, Square> board = EmptySquaresMaker.make();
        Piece attackerPiece = King.from(Color.WHITE);
        Piece attackedPiece = King.from(Color.BLACK);
        board.put(new Position(startRank, startFile), attackerPiece);
        board.put(new Position(targetRank, targetFile), attackedPiece);
        Path path = new Path(
                new Position(startRank, startFile), new Position(targetRank, targetFile));

        final boolean canAttack = attackerPiece.canArrive(path, board);

        assertThat(canAttack).isTrue();
    }
}
