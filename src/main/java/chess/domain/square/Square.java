package chess.domain.square;

import chess.domain.position.Path;
import chess.domain.position.Position;

import chess.domain.square.piece.Color;
import java.util.Map;

public interface Square {
    boolean canMove(Path path, Map<Position, Square> board);

    boolean isColor(Color color);
}
