package chess.domain;

import chess.domain.board.ChessBoard;
import chess.domain.position.Path;
import chess.domain.position.Position;
import chess.domain.square.Score;
import chess.domain.square.Square;
import chess.domain.square.piece.Color;

import java.util.Map;

public class ChessGame {
    private CurrentTurn currentTurn;
    private final ChessBoard chessBoard;

    public ChessGame(CurrentTurn currentTurn, ChessBoard chessBoard) {
        this.currentTurn = currentTurn;
        this.chessBoard = chessBoard;
    }

    public void move(Path path) {
        validateIsFriendly(path);
        chessBoard.move(path);
        currentTurn = currentTurn.change();
    }

    private void validateIsFriendly(Path path) {
        Square startSquare = chessBoard.findSquare(path.startPosition());
        if (!startSquare.isColor(currentTurn.value())) {
            throw new IllegalArgumentException("시작 위치에 아군 체스말이 존재해야 합니다.");
        }
    }

    public Score calculateScore(Color color) {
        return chessBoard.calculateScore(color);
    }

    public boolean isOver() {
        return chessBoard.isAnyKingDead();
    }

    public Color findWinner() {
        if (chessBoard.isKingDead(Color.WHITE)) {
            return Color.BLACK;
        }
        if (chessBoard.isKingDead(Color.BLACK)) {
            return Color.WHITE;
        }
        throw new IllegalStateException("왕이 모두 살아있으면 우승자를 정할 수 없습니다.");
    }

    public Map<Position, Square> getSurvivedPieces() {
        return chessBoard.getPieces();
    }

    public CurrentTurn getCurrentTurn() {
        return currentTurn;
    }

    public Map<Position, Square> getBoard() {
        return chessBoard.getSquares();
    }
}
