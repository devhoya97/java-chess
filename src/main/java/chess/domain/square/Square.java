package chess.domain.square;

import chess.domain.position.Path;
import chess.domain.position.Position;

import chess.domain.square.piece.Color;

import java.util.Map;
import java.util.Set;

public interface Square {
    boolean canArrive(Path path, Map<Position, Square> board);

    boolean isColor(Color color);

    double score(Set<Square> sameFileSquares);
}
