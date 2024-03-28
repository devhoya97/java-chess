package chess.domain.square.piece;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.EmptySquaresMaker;
import chess.domain.position.File;
import chess.domain.position.Path;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.domain.square.Square;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class PawnTest {

    @Nested
    class BlackPawnTest {
        @DisplayName("한 번도 이동하지 않은 블랙 폰은 밑으로 두 칸 움직일 수 있다.")
        @Test
        void CanMoveTwoStepDownIfFirstMove() {
            final Map<Position, Square> board = EmptySquaresMaker.make();
            Piece piece = Pawn.from(Color.BLACK);
            board.put(new Position(Rank.SEVENTH, File.A), piece);
            Path path = new Path(
                    new Position(Rank.SEVENTH, File.A), new Position(Rank.FIFTH, File.A));

            final boolean canMove = piece.canArrive(path, board);

            assertThat(canMove).isTrue();
        }

        @DisplayName("이동한적이 있는 블랙 폰은 밑으로 두 칸 움직일 수 없다.")
        @Test
        void CanNotMoveTwoStepDownUnlessFirstMove() {
            final Map<Position, Square> board = EmptySquaresMaker.make();
            Piece piece = Pawn.from(Color.BLACK);
            board.put(new Position(Rank.FIFTH, File.A), piece);
            Path path = new Path(
                    new Position(Rank.FIFTH, File.A), new Position(Rank.THIRD, File.A));

            final boolean canMove = piece.canArrive(path, board);

            assertThat(canMove).isFalse();
        }

        @DisplayName("블랙 폰은 항상 밑으로 한 칸 움직일 수 있다.")
        @Test
        void CanMoveOneStepDown() {
            final Map<Position, Square> board = EmptySquaresMaker.make();
            Piece piece = Pawn.from(Color.BLACK);
            board.put(new Position(Rank.SEVENTH, File.A), piece);
            Path path = new Path(
                    new Position(Rank.SEVENTH, File.A), new Position(Rank.SIXTH, File.A));

            final boolean canMove = piece.canArrive(path, board);

            assertThat(canMove).isTrue();
        }

        @DisplayName("블랙 폰은 밑으로 한 칸 또는 두 칸을 제외하고 움직일 수 없다.")
        @ParameterizedTest
        @MethodSource("provideUnValidPathForBlack")
        void CanNotMoveUnlessOneOrTwoStepDown(Path path) {
            final Map<Position, Square> board = EmptySquaresMaker.make();
            Piece piece = Pawn.from(Color.BLACK);
            board.put(new Position(Rank.SEVENTH, File.B), piece);

            final boolean canMove = piece.canArrive(path, board);

            assertThat(canMove).isFalse();
        }

        static Stream<Path> provideUnValidPathForBlack() {
            return Stream.of(
                    new Path(new Position(Rank.SEVENTH, File.B), new Position(Rank.EIGHTH, File.B)),
                    new Path(new Position(Rank.SEVENTH, File.B), new Position(Rank.SEVENTH, File.A)),
                    new Path(new Position(Rank.SEVENTH, File.B), new Position(Rank.SEVENTH, File.C)),
                    new Path(new Position(Rank.SEVENTH, File.B), new Position(Rank.SIXTH, File.A)),
                    new Path(new Position(Rank.SEVENTH, File.B), new Position(Rank.SIXTH, File.C))
            );
        }

        @DisplayName("블랙 폰은 대각선 한 칸 밑을 공격할 수 있다.")
        @ParameterizedTest
        @CsvSource({
                "SEVENTH, B, SIXTH, A",
                "SEVENTH, B, SIXTH, C",
        })
        void CanAttackOneStepDownDiagonal(Rank startRank, File startFile, Rank targetRank, File targetFile) {
            final Map<Position, Square> board = EmptySquaresMaker.make();
            Piece attackerPiece = Pawn.from(Color.BLACK);
            Piece attackedPiece = Pawn.from(Color.WHITE);
            board.put(new Position(startRank, startFile), attackerPiece);
            board.put(new Position(targetRank, targetFile), attackedPiece);
            Path path = new Path(
                    new Position(startRank, startFile), new Position(targetRank, targetFile));

            final boolean canAttack = attackerPiece.canArrive(path, board);

            assertThat(canAttack).isTrue();
        }

        @DisplayName("블랙 폰은 대각선 한 칸 밑을 제외하고 공격할 수 없다.")
        @ParameterizedTest
        @MethodSource("provideUnValidAttackedPositionForBlack")
        void CanNotAttackUnlessOneStepDownDiagonal(Position attackedPosition) {
            final Map<Position, Square> board = EmptySquaresMaker.make();
            Path path = new Path(new Position(Rank.SEVENTH, File.B), attackedPosition);
            Piece attackerPiece = Pawn.from(Color.BLACK);
            Piece attackedPiece = Pawn.from(Color.WHITE);
            board.put(new Position(Rank.SEVENTH, File.B), attackerPiece);
            board.put(attackedPosition, attackedPiece);

            final boolean canAttack = attackerPiece.canArrive(path, board);

            assertThat(canAttack).isFalse();
        }

        static Stream<Position> provideUnValidAttackedPositionForBlack() {
            return Stream.of(
                    new Position(Rank.EIGHTH, File.A),
                    new Position(Rank.EIGHTH, File.C),
                    new Position(Rank.SIXTH, File.B),
                    new Position(Rank.EIGHTH, File.B),
                    new Position(Rank.SEVENTH, File.A),
                    new Position(Rank.SEVENTH, File.C)
            );
        }
    }

    @Nested
    class WhitePawnTest {

        @DisplayName("한 번도 이동하지 않은 화이트 폰은 위로 두 칸 움직일 수 있다.")
        @Test
        void CanMoveTwoStepUpIfFirstMove() {
            final Map<Position, Square> board = EmptySquaresMaker.make();
            Piece piece = Pawn.from(Color.WHITE);
            board.put(new Position(Rank.SECOND, File.A), piece);
            Path path = new Path(
                    new Position(Rank.SECOND, File.A), new Position(Rank.FOURTH, File.A));

            final boolean canMove = piece.canArrive(path, board);

            assertThat(canMove).isTrue();
        }

        @DisplayName("이동한적이 있는 화이트 폰은 위로 두 칸 움직일 수 없다.")
        @Test
        void CanNotMoveTwoStepUpUnlessFirstMove() {
            final Map<Position, Square> board = EmptySquaresMaker.make();
            Piece piece = Pawn.from(Color.WHITE);
            board.put(new Position(Rank.FOURTH, File.A), piece);
            Path path = new Path(new Position(Rank.FOURTH, File.A), new Position(Rank.SIXTH, File.A));

            final boolean canMove = piece.canArrive(path, board);

            assertThat(canMove).isFalse();
        }

        @DisplayName("화이트 폰은 항상 위로 한 칸 움직일 수 있다.")
        @Test
        void CanMoveOneStepUp() {
            final Map<Position, Square> board = EmptySquaresMaker.make();
            Piece piece = Pawn.from(Color.WHITE);
            board.put(new Position(Rank.SECOND, File.A), piece);
            Path path = new Path(new Position(Rank.SECOND, File.A), new Position(Rank.THIRD, File.A));

            final boolean canMove = piece.canArrive(path, board);

            assertThat(canMove).isTrue();
        }

        @DisplayName("화이트 폰은 위로 한 칸 또는 두 칸을 제외하고 움직일 수 없다.")
        @ParameterizedTest
        @MethodSource("provideUnValidPathForWhite")
        void canNotMoveUnlessOneOrTwoStepUp(Path path) {
            final Map<Position, Square> board = EmptySquaresMaker.make();
            Piece piece = Pawn.from(Color.WHITE);
            board.put(new Position(Rank.SECOND, File.B), piece);

            final boolean canMove = piece.canArrive(path, board);

            assertThat(canMove).isFalse();
        }

        static Stream<Path> provideUnValidPathForWhite() {
            return Stream.of(
                    new Path(new Position(Rank.SECOND, File.B), new Position(Rank.FIRST, File.B)),
                    new Path(new Position(Rank.SECOND, File.B), new Position(Rank.SECOND, File.A)),
                    new Path(new Position(Rank.SECOND, File.B), new Position(Rank.SECOND, File.C)),
                    new Path(new Position(Rank.SECOND, File.B), new Position(Rank.FIRST, File.A)),
                    new Path(new Position(Rank.SECOND, File.B), new Position(Rank.FIRST, File.C))
            );
        }

        @DisplayName("화이트 폰은 대각선 한 칸 위를 공격할 수 있다.")
        @ParameterizedTest
        @CsvSource({
                "SECOND, B, THIRD, A",
                "SECOND, B, THIRD, C",
        })
        void CanAttackOneStepDownDiagonal(Rank startRank, File startFile, Rank targetRank, File targetFile) {
            final Map<Position, Square> board = EmptySquaresMaker.make();
            Piece attackerPiece = Pawn.from(Color.WHITE);
            Piece attackedPiece = Pawn.from(Color.BLACK);
            board.put(new Position(startRank, startFile), attackerPiece);
            board.put(new Position(targetRank, targetFile), attackedPiece);
            Path path = new Path(
                    new Position(startRank, startFile), new Position(targetRank, targetFile));

            final boolean canAttack = attackerPiece.canArrive(path, board);

            assertThat(canAttack).isTrue();
        }

        @DisplayName("화이트 폰은 대각선 한 칸 위를 제외하고 공격할 수 없다.")
        @ParameterizedTest
        @MethodSource("provideUnValidAttackedPositionForWhite")
        void CanNotAttackUnlessOneStepDiagonal(Position attackedPosition) {
            final Map<Position, Square> board = EmptySquaresMaker.make();
            Path path = new Path(new Position(Rank.SEVENTH, File.B), attackedPosition);
            Piece attackerPiece = Pawn.from(Color.WHITE);
            Piece attackedPiece = Pawn.from(Color.BLACK);
            board.put(new Position(Rank.SEVENTH, File.B), attackerPiece);
            board.put(attackedPosition, attackedPiece);

            final boolean canAttack = attackerPiece.canArrive(path, board);

            assertThat(canAttack).isFalse();
        }

        static Stream<Position> provideUnValidAttackedPositionForWhite() {
            return Stream.of(
                    new Position(Rank.SIXTH, File.A),
                    new Position(Rank.SIXTH, File.C),
                    new Position(Rank.SIXTH, File.B),
                    new Position(Rank.EIGHTH, File.B),
                    new Position(Rank.SEVENTH, File.A),
                    new Position(Rank.SEVENTH, File.C)
            );
        }
    }
}
