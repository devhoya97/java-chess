package chess.domain.square;

import chess.domain.position.Path;
import chess.domain.position.Position;

import chess.domain.square.piece.Color;
import java.util.Map;
import java.util.Set;

public class Empty implements Square {
    private static final Empty INSTANCE = new Empty();

    private Empty() {
    }

    public static Empty getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean canArrive(Path path, Map<Position, Square> board) {
        return false;
    }

    @Override
    public boolean isColor(final Color color) {
        return false;
    }

    @Override
    public Score score(Set<Square> sameFileSquares) {
        return Score.of(0, Color.NO_COLOR);
    }

    @Override
    public boolean isKing() {
        return false;
    }

    @Override
    public boolean isPiece() {
        return false;
    }

    @Override
    public Color getColor() {
        return Color.NO_COLOR;
    }
}
