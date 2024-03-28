package chess.domain.square.piece;

import chess.domain.position.Path;
import chess.domain.position.Position;
import chess.domain.square.Empty;
import chess.domain.square.Score;
import chess.domain.square.Square;

import java.util.Map;
import java.util.Set;

public class Pawn extends Piece {
    private static final int ATTACKABLE_FILE_DISTANCE = 1;
    private static final int NORMAL_MOVABLE_DISTANCE = 1;
    private static final int FIRST_MOVABLE_MAX_DISTANCE = 2;
    private static final int ATTACKABLE_RANK_DISTANCE = 1;
    private static final int BLACK_START_RANK = 7;
    private static final int WHITE_START_RANK = 2;
    private static final int DOWN_DIRECTION = -1;
    private static final double DEFAULT_SCORE = 1.0;
    private static final double DEDUCTED_SCORE = 0.5;
    private static final Map<Color, Pawn> PAWN_POOL = Map.of(
            Color.WHITE, new Pawn(Color.WHITE),
            Color.BLACK, new Pawn(Color.BLACK));

    private Pawn(Color color) {
        super(color);
    }

    public static Pawn from(Color color) {
        return PAWN_POOL.get(color);
    }

    @Override
    public boolean canArrive(Path path, Map<Position, Square> board) {
        final Square targetSquare = board.get(path.targetPosition());
        if (targetSquare.isColor(getColor())) {
            return false;
        }
        if (targetSquare == Empty.getInstance()) {
            return canMove(path) && isNotObstructed(path, board);
        }
        return canAttack(path) && isNotObstructed(path, board);
    }

    @Override
    protected boolean canMove(Path path) {
        if (isColor(Color.BLACK)) {
            return path.isDown(maxDistance(path, BLACK_START_RANK));
        }
        return path.isUp(maxDistance(path, WHITE_START_RANK));
    }

    private int maxDistance(Path path, int startRank) {
        if (path.isStartRank(startRank)) {
            return FIRST_MOVABLE_MAX_DISTANCE;
        }
        return NORMAL_MOVABLE_DISTANCE;
    }

    private boolean canAttack(Path path) {
        int attackableRankDiff = ATTACKABLE_RANK_DISTANCE;
        if (isColor(Color.BLACK)) {
            attackableRankDiff *= DOWN_DIRECTION;
        }
        return (path.subtractRank() == attackableRankDiff) &&
                (path.fileDistance() == ATTACKABLE_FILE_DISTANCE);
    }

    @Override
    public Score score(Set<Square> sameFileSquares) {
        long count = sameFileSquares.stream()
                .filter(square -> square == this)
                .count();
        if (count == 0) {
            return Score.from(DEFAULT_SCORE);
        }
        return Score.from(DEDUCTED_SCORE);
    }
}
