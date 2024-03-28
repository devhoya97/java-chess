package chess.domain.square.piece.unified;

import chess.domain.position.Path;
import chess.domain.square.Score;
import chess.domain.square.Square;
import chess.domain.square.piece.Color;

import java.util.Map;
import java.util.Set;

public class King extends MoveAttackUnified {
    private static final int MOVABLE_DISTANCE = 1;
    private static final double SCORE = 0;
    private static final Map<Color, King> KING_POOL = Map.of(
            Color.WHITE, new King(Color.WHITE),
            Color.BLACK, new King(Color.BLACK));

    private King(Color color) {
        super(color);
    }

    public static King from(Color color) {
        return KING_POOL.get(color);
    }

    @Override
    protected boolean canMove(Path path) {
        return path.isStraight(MOVABLE_DISTANCE) || path.isDiagonal(MOVABLE_DISTANCE);
    }

    @Override
    public Score score(Set<Square> sameFileSquares) {
        return Score.from(SCORE);
    }
}
