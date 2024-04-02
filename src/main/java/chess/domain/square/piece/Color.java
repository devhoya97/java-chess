package chess.domain.square.piece;

import java.util.Arrays;
import java.util.Objects;

public enum Color {
    BLACK, WHITE, NO_COLOR;

    public Color opposite() {
        if (this == BLACK) {
            return WHITE;
        }
        return BLACK;
    }

    public static Color findByName(String colorName) {
        return Arrays.stream(Color.values())
                .filter(color -> Objects.equals(color.name(), colorName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("[" + colorName + "]와(과) 매칭되는 Color가 없습니다."));
    }
}
