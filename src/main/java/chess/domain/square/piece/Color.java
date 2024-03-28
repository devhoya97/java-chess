package chess.domain.square.piece;

public enum Color {
    BLACK, WHITE, NO_COLOR;

    public Color opposite() {
        if (this == BLACK) {
            return WHITE;
        }
        return BLACK;
    }
}
