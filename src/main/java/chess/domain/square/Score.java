package chess.domain.square;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Score {
    private final double value;
    private static final Map<Double, Score> CACHE;

    static {
        CACHE = new HashMap<>();
        CACHE.put(0.5, new Score(0.5));
        CACHE.put(1.0, new Score(1.0));
        CACHE.put(2.5, new Score(2.5));
        CACHE.put(3.0, new Score(3.0));
        CACHE.put(5.0, new Score(5.0));
        CACHE.put(9.0, new Score(9.0));
    }

    private Score(double value) {
        this.value = value;
    }

    public static Score from(double value) {
        if (CACHE.containsKey(value)) {
            return CACHE.get(value);
        }
        return new Score(value);
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score = (Score) o;
        return Double.compare(value, score.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
