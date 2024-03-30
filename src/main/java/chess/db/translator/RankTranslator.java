package chess.db.translator;

import chess.domain.position.Rank;

import java.util.Arrays;
import java.util.Objects;

public enum RankTranslator {
    FIRST("1", Rank.FIRST),
    SECOND("2", Rank.SECOND),
    THIRD("3", Rank.THIRD),
    FOURTH("4", Rank.FOURTH),
    FIFTH("5", Rank.FIFTH),
    SIXTH("6", Rank.SIXTH),
    SEVENTH("7", Rank.SEVENTH),
    EIGHTH("8", Rank.EIGHTH);

    private final String dbView;
    private final Rank rank;

    RankTranslator(final String dbView, final Rank rank) {
        this.dbView = dbView;
        this.rank = rank;
    }

    public static String translate(Rank rank) {
        return Arrays.stream(RankTranslator.values())
                .filter(value -> Objects.equals(value.rank, rank))
                .findAny()
                .map(value -> value.dbView)
                .orElseThrow(() -> new IllegalArgumentException("준비된 rank가 아닙니다."));
    }

    public static Rank translate(String dbView) {
        return Arrays.stream(RankTranslator.values())
                .filter(value -> Objects.equals(value.dbView, dbView))
                .findAny()
                .map(value -> value.rank)
                .orElseThrow(() -> new IllegalArgumentException("dbView(" + dbView + ")와 매칭되는 rank가 없습니다."));
    }
}
