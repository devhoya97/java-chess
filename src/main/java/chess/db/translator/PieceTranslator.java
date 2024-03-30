package chess.db.translator;

import chess.domain.square.Square;
import chess.domain.square.piece.Color;
import chess.domain.square.piece.Pawn;
import chess.domain.square.piece.Piece;
import chess.domain.square.piece.unified.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public enum PieceTranslator {
    BISHOP("bishop", Bishop::from, Bishop.class),
    KING("king", King::from, King.class),
    KNIGHT("knight", Knight::from, Knight.class),
    QUEEN("queen", Queen::from, Queen.class),
    ROOK("rook", Rook::from, Rook.class),
    PAWN("pawn", Pawn::from, Pawn.class);

    private final String dbView;
    private final Function<Color, Piece> translator;
    private final Class<? extends Piece> pieceClass;

    PieceTranslator(String dbView, Function<Color, Piece> translator, Class<? extends Piece> pieceClass) {
        this.dbView = dbView;
        this.translator = translator;
        this.pieceClass = pieceClass;
    }

    public static Piece translate(String dbView, Color color) {
        return Arrays.stream(PieceTranslator.values())
                .filter(pieceTranslator -> Objects.equals(pieceTranslator.dbView, dbView))
                .findAny()
                .map(pieceTranslator -> pieceTranslator.translator.apply(color))
                .orElseThrow(() -> new IllegalArgumentException("준비된 PieceType이 아닙니다."));
    }

    public static String translate(Square square) {
        return Arrays.stream(PieceTranslator.values())
                .filter(pieceTranslator -> Objects.equals(square.getClass(), pieceTranslator.pieceClass))
                .map(pieceTranslator -> pieceTranslator.dbView)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("준비된 PieceType이 아닙니다."));
    }
}
