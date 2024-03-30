package chess.db.translator;

import chess.domain.square.piece.Color;

import java.util.Arrays;
import java.util.Objects;

public enum ColorTranslator {
    WHITE("white", Color.WHITE),
    BLACK("black", Color.BLACK);

    private final String dbView;
    private final Color color;

    ColorTranslator(String dbView, Color color) {
        this.dbView = dbView;
        this.color = color;
    }

    public static String translate(Color color) {
        return Arrays.stream(ColorTranslator.values())
                .filter(colorTranslator -> colorTranslator.color == color)
                .map(colorTranslator -> colorTranslator.dbView)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("준비된 Color가 아닙니다."));
    }

    public static Color translate(String dbView) {
        return Arrays.stream(ColorTranslator.values())
                .filter(colorTranslator -> Objects.equals(colorTranslator.dbView, dbView))
                .map(colorTranslator -> colorTranslator.color)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("dbView(" + dbView + ")와 매칭되는 Color가 없습니다."));
    }
}
