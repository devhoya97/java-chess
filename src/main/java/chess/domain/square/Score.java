package chess.domain.square;

import chess.domain.square.piece.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Score {
    private final double value;
    private final Color color;
    private static final Map<String , Score> CACHE;

    static {
        CACHE = new HashMap<>();
        CACHE.putAll(makeScoreBy(Color.WHITE));
        CACHE.putAll(makeScoreBy(Color.BLACK));
    }

    static Map<String, Score> makeScoreBy(Color color) {
        Map<String, Score> scores = new HashMap<>();
        scores.put(toKey(0.5, color), new Score(0.5, color));
        scores.put(toKey(1.0, color), new Score(1.0, color));
        scores.put(toKey(2.5, color), new Score(2.5, color));
        scores.put(toKey(3.0, color), new Score(3.0, color));
        scores.put(toKey(5.0, color), new Score(5.0, color));
        scores.put(toKey(9.0, color), new Score(9.0, color));
        return scores;
    }

    static String toKey(double value, Color color) {
        return "" + value + color;
    }

    private Score(double value, Color color) {
        this.value = value;
        this.color = color;
    }

    public static Score of(double value, Color color) {
        if (CACHE.containsKey(toKey(value, color))) {
            return CACHE.get(toKey(value, color));
        }
        return new Score(value, color);
    }


    public Color findWinner(Score other) {
        validateOpposite(other);
        if (value > other.value) {
            return color;
        }
        if (value < other.value) {
            return other.color;
        }
        return Color.NO_COLOR;
    }

    private void validateOpposite(Score other) {
        if (color.opposite() != other.color) {
            throw new IllegalArgumentException("서로 다른 색의 점수를 입력해야 승자를 구분할 수 있습니다.");
        }
    }

    public double getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score = (Score) o;
        return Double.compare(value, score.value) == 0 && color == score.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, color);
    }
}
