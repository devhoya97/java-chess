package chess.domain;

import chess.domain.board.ChessBoard;
import chess.domain.position.Path;
import chess.domain.position.Position;
import chess.domain.square.Square;

import java.util.Map;

public class ChessGame {
    private final CurrentTurn currentTurn;
    private final ChessBoard chessBoard;

    public ChessGame(CurrentTurn currentTurn, ChessBoard chessBoard) {
        this.currentTurn = currentTurn;
        this.chessBoard = chessBoard;
    }

    public void move(Path path) {
        validateIsFriendly(path);
        chessBoard.move(path);
        currentTurn.change();
    }

    private void validateIsFriendly(Path path) {
        Square startSquare = chessBoard.findSquare(path.startPosition());
        if (!startSquare.isColor(currentTurn.value())) {
            throw new IllegalArgumentException("시작 위치에 아군 체스말이 존재해야 합니다.");
        }
    }

    public Map<Position, Square> getBoard() {
        return chessBoard.getSquares();
    }
}
