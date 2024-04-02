package chess.db;

import chess.db.translator.PieceTranslator;
import chess.domain.position.File;
import chess.domain.position.Rank;
import chess.domain.square.Square;
import chess.domain.square.piece.Color;

public record PieceInfo(String colorName, String fileName, String rankName, String pieceName) {
    public static PieceInfo of(Color color, File file, Rank rank, Square square) {
        return new PieceInfo(color.name(), file.name(), rank.name(), PieceTranslator.translate(square));
    }
}
