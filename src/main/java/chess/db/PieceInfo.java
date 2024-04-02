package chess.db;

import chess.db.translator.PieceTranslator;
import chess.domain.position.File;
import chess.domain.position.Rank;
import chess.domain.square.Square;
import chess.domain.square.piece.Color;
import chess.domain.square.piece.Piece;

public record PieceInfo(String colorName, String fileName, String rankName, String pieceName) {
    public static PieceInfo of(Color color, File file, Rank rank, Square square) {
        return new PieceInfo(color.name(), file.name(), rank.name(), PieceTranslator.translateToName(square));
    }

    public Color color() {
        return Color.findByName(colorName);
    }

    public File file() {
        return File.findByName(fileName);
    }

    public Rank rank() {
        return Rank.findByName(rankName);
    }

    public Piece piece() {
        return PieceTranslator.translateToPiece(pieceName, color());
    }
}
