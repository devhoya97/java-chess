package chess.domain;

import chess.domain.board.ChessBoard;
import chess.domain.position.File;
import chess.domain.position.Path;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.domain.square.Square;
import chess.domain.square.piece.Color;
import chess.domain.square.piece.unified.King;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ChessGameTest {

    @DisplayName("체스말을 움직일 때, 시작 위치에 아군 말이 존재하지 않는 경우 예외를 발생시킨다.")
    @Test
    void startEmptyExceptionTest() {
        ChessGame chessGame = new ChessGame(new CurrentTurn(Color.WHITE), new ChessBoard(EmptySquaresMaker.make()));
        Path path = new Path(new Position(Rank.FIRST, File.A), new Position(Rank.SECOND, File.B));

        assertThatThrownBy(() -> chessGame.move(path))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작 위치에 아군 체스말이 존재해야 합니다.");
    }

    @DisplayName("양쪽 킹이 살아있으면 아직 승부가 나지 않는다.")
    @Test
    void noOneWinIfBothKingAlive() {
        final Map<Position, Square> squares = EmptySquaresMaker.make();
        squares.put(new Position(Rank.FIRST, File.A), King.from(Color.WHITE));
        squares.put(new Position(Rank.FIRST, File.E), King.from(Color.BLACK));
        ChessGame chessGame = new ChessGame(new CurrentTurn(Color.WHITE), new ChessBoard(squares));

        assertThatThrownBy(chessGame::findWinner).isInstanceOf(IllegalStateException.class);
    }

    @DisplayName("화이트킹이 죽으면 블랙진영의 승리이다.")
    @Test
    void blackWinIfWhiteDied() {
        final Map<Position, Square> squares = EmptySquaresMaker.make();
        squares.put(new Position(Rank.FIRST, File.E), King.from(Color.BLACK));
        ChessGame chessGame = new ChessGame(new CurrentTurn(Color.WHITE), new ChessBoard(squares));

        Color winner = chessGame.findWinner();

        assertThat(winner).isEqualTo(Color.BLACK);
    }
}
