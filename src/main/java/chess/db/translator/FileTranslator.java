package chess.db.translator;

import chess.domain.position.File;

import java.util.Arrays;
import java.util.Objects;

public enum FileTranslator {
    A("a", File.A),
    B("b", File.B),
    C("c", File.C),
    D("d", File.D),
    E("e", File.E),
    F("f", File.F),
    G("g", File.G),
    H("h", File.H);

    private final String dbView;
    private final File file;

    FileTranslator(final String dbView, final File file) {
        this.dbView = dbView;
        this.file = file;
    }

    public static String translate(File file) {
        return Arrays.stream(FileTranslator.values())
                .filter(value -> Objects.equals(value.file, file))
                .findAny()
                .map(value -> value.dbView)
                .orElseThrow(() -> new IllegalArgumentException("준비된 file이 아닙니다."));
    }

    public static File translate(String dbView) {
        return Arrays.stream(FileTranslator.values())
                .filter(value -> Objects.equals(value.dbView, dbView))
                .findAny()
                .map(value -> value.file)
                .orElseThrow(() -> new IllegalArgumentException("dbView(" + dbView + ")와 매칭되는 file이 없습니다."));
    }
}
