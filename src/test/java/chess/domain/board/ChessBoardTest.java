package chess.domain.board;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.EmptySquaresMaker;
import chess.domain.position.File;
import chess.domain.position.Path;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.domain.square.Score;
import chess.domain.square.Square;
import chess.domain.square.piece.Color;
import chess.domain.square.piece.Pawn;
import chess.domain.square.piece.unified.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChessBoardTest {

    @DisplayName("target이 비어있는 경우 체스말을 움직인다.")
    @Test
    void movePieceTest() {
        final Map<Position, Square> squares = EmptySquaresMaker.make();
        squares.put(new Position(Rank.FIRST, File.A), Rook.from(Color.WHITE));
        ChessBoard chessBoard = new ChessBoard(squares);
        Map<Position, Square> expected = EmptySquaresMaker.make();
        expected.put(new Position(Rank.FIRST, File.B), Rook.from(Color.WHITE));

        chessBoard.move(new Path(new Position(Rank.FIRST, File.A), new Position(Rank.FIRST, File.B)));

        assertThat(chessBoard.getSquares()).isEqualTo(expected);
    }

    @DisplayName("target이 체스말인 경우 공격한다.")
    @Test
    void moveAttackTest() {
        final Map<Position, Square> squares = EmptySquaresMaker.make();
        squares.put(new Position(Rank.FIRST, File.A), Rook.from(Color.WHITE));
        squares.put(new Position(Rank.FIRST, File.B), Queen.from(Color.BLACK));
        ChessBoard chessBoard = new ChessBoard(squares);
        Map<Position, Square> expected = EmptySquaresMaker.make();
        expected.put(new Position(Rank.FIRST, File.B), Rook.from(Color.WHITE));

        chessBoard.move(new Path(new Position(Rank.FIRST, File.A), new Position(Rank.FIRST, File.B)));

        assertThat(chessBoard.getSquares()).isEqualTo(expected);
    }

    @DisplayName("각 진영의 점수를 계산한다.")
    @Test
    void calculateColorScore() {
        final Map<Position, Square> squares = EmptySquaresMaker.make();
        squares.put(new Position(Rank.FIRST, File.A), Queen.from(Color.BLACK));
        squares.put(new Position(Rank.FIRST, File.B), Rook.from(Color.BLACK));
        squares.put(new Position(Rank.FIRST, File.C), Bishop.from(Color.BLACK));
        squares.put(new Position(Rank.FIRST, File.D), Knight.from(Color.BLACK));
        squares.put(new Position(Rank.FIRST, File.E), King.from(Color.BLACK));
        squares.put(new Position(Rank.FIRST, File.F), Pawn.from(Color.BLACK));
        squares.put(new Position(Rank.SECOND, File.F), Pawn.from(Color.BLACK));
        ChessBoard chessBoard = new ChessBoard(squares);

        Score score = chessBoard.calculateScore(Color.BLACK);

        assertThat(score).isEqualTo(Score.of(20.5, Color.BLACK));
    }
}
