package chess.db;

import chess.db.translator.ColorTranslator;
import chess.db.translator.FileTranslator;
import chess.db.translator.PieceTranslator;
import chess.db.translator.RankTranslator;
import chess.domain.board.ChessBoard;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.domain.square.Empty;
import chess.domain.square.Square;
import chess.domain.square.piece.Piece;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChessBoardLoader {
    private final Map<Position, Square> pieces;

    public ChessBoardLoader(Map<Position, Square> pieces) {
        this.pieces = pieces;
    }

    public static ChessBoardLoader from(Set<PieceInfo> prevSurvivedPieces) {
        Map<Position, Square> pieces = new HashMap<>();
        for (PieceInfo pieceInfo : prevSurvivedPieces) {
            Position position = new Position(
                    RankTranslator.translate(pieceInfo.rank()),
                    FileTranslator.translate(pieceInfo.file()));
            Piece piece = PieceTranslator.translate(
                    pieceInfo.pieceType(),
                    ColorTranslator.translate(pieceInfo.color()));
            pieces.put(position, piece);
        }
        return new ChessBoardLoader(pieces);
    }

    public ChessBoard load() {
        Map<Position, Square> board = makeEmptyBoard();
        board.putAll(pieces);
        return new ChessBoard(board);
    }

    private Map<Position, Square> makeEmptyBoard() {
        final Map<Position, Square> squares = new HashMap<>();
        for (Rank rank : Rank.values()) {
            squares.putAll(makeEmptyRank(rank));
        }
        return squares;
    }

    private Map<Position, Square> makeEmptyRank(final Rank rank) {
        final Map<Position, Square> squares = new HashMap<>();
        for (File file : File.values()) {
            squares.put(new Position(rank, file), Empty.getInstance());
        }
        return squares;
    }
}
