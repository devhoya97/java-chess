package chess.domain.position;

import java.util.Arrays;

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

    public int calculateDiff(File file) {
        return Math.abs(file.value - value);
    }

    public int getValue() {
        return value;
    }
}
