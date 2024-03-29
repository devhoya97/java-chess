package chess.domain.square.piece.unified;

import chess.domain.position.Path;
import chess.domain.square.Score;
import chess.domain.square.Square;
import chess.domain.square.piece.Color;

import java.util.Map;
import java.util.Set;

public class Rook extends MoveAttackUnified {
    private static final double SCORE = 5.0;
    private static final Map<Color, Rook> ROOK_POOL = Map.of(
            Color.WHITE, new Rook(Color.WHITE),
            Color.BLACK, new Rook(Color.BLACK));

    private Rook(Color color) {
        super(color);
    }

    public static Rook from(Color color) {
        return ROOK_POOL.get(color);
    }

    @Override
    protected boolean canMove(Path path) {
        return path.isStraight();
    }

    @Override
    public Score score(Set<Square> sameFileSquares) {
        return Score.of(SCORE, getColor());
    }

    @Override
    public boolean isKing() {
        return false;
    }
}
