package chess.domain.position;

import java.util.Arrays;
import java.util.Objects;

public enum File {
    A(1),
    B(2),
    C(3),
    D(4),
    E(5),
    F(6),
    G(7),
    H(8);

    private final int value;

    File(int value) {
        this.value = value;
    }

    public static File from(int value) {
        return Arrays.stream(values())
                .filter(file -> file.value == value)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("파일은 1에서 8 사이의 숫자이어야 합니다."));
    }

    public static File findByName(String fileName) {
        return Arrays.stream(File.values())
                .filter(file -> Objects.equals(file.name(), fileName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("[" + fileName + "]와(과) 매칭되는 file이 없습니다."));
    }

    public int calculateDistance(File file) {
        return Math.abs(subtract(file));
    }

    public int subtract(File file) {
        return value - file.value;
    }

    public int getValue() {
        return value;
    }
}
