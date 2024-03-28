package chess.domain.square.piece.unified;

import chess.domain.position.Path;
import chess.domain.position.Position;
import chess.domain.square.Square;
import chess.domain.square.piece.Color;
import chess.domain.square.piece.Piece;
import java.util.Map;

public abstract class MoveAttackUnified extends Piece {

    public MoveAttackUnified(final Color color) {
        super(color);
    }

    @Override
    public final boolean canArrive(Path path, Map<Position, Square> board) {
        final Square targetSquare = board.get(path.targetPosition());
        if (targetSquare.isColor(getColor())) {
            return false;
        }
        return canMove(path) && isNotObstructed(path, board);
    }
}
