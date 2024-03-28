package chess.domain.square.piece.unified;

import chess.domain.position.Path;
import chess.domain.square.Square;
import chess.domain.square.piece.Color;

import java.util.List;
import java.util.Map;

public class Bishop extends MoveAttackUnified {
    private static final double SCORE = 3.0;
    private static final Map<Color, Bishop> BISHOP_POOL = Map.of(
            Color.WHITE, new Bishop(Color.WHITE),
            Color.BLACK, new Bishop(Color.BLACK));

    private Bishop(Color color) {
        super(color);
    }

    public static Bishop from(Color color) {
        return BISHOP_POOL.get(color);
    }

    @Override
    protected boolean canMove(Path path) {
        return path.isDiagonal();
    }

    public double score(List<Square> sameFileSquares) {
        return SCORE;
    }
}
