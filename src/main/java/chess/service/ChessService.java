package chess.service;

import chess.db.*;
import chess.db.dao.CurrentTurnDao;
import chess.db.dao.PieceInfoDao;
import chess.domain.ChessGame;
import chess.domain.CurrentTurn;
import chess.domain.board.ChessBoard;
import chess.domain.board.ChessBoardMaker;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import chess.domain.square.Empty;
import chess.domain.square.Square;
import chess.domain.square.piece.Color;
import chess.domain.square.piece.Piece;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChessService {
    private final PieceInfoDao pieceInfoDao;
    private final CurrentTurnDao currentTurnDao;

    public ChessService(PieceInfoDao pieceInfoDao, CurrentTurnDao currentTurnDao) {
        this.pieceInfoDao = pieceInfoDao;
        this.currentTurnDao = currentTurnDao;
    }

    public static ChessService create() {
        DBConnector dbConnector = new DBConnector();
        return new ChessService(new PieceInfoDao(dbConnector), new CurrentTurnDao(dbConnector));
    }

    public ChessGame loadGame() {
        if (pieceInfoDao.isEmpty()) {
            return makeNewGame();
        }
        return loadPrevGame();
    }

    private static ChessGame makeNewGame() {
        ChessBoardMaker chessBoardMaker = new ChessBoardMaker();
        return new ChessGame(new CurrentTurn(Color.WHITE), chessBoardMaker.make());
    }

    private ChessGame loadPrevGame() {
        Set<PieceInfo> survivedPieceInfos = pieceInfoDao.findAll();
        Map<Position, Square> newBoard = makeEmptyBoard();
        for (PieceInfo survivedPieceInfo : survivedPieceInfos) {
            Position position = new Position(survivedPieceInfo.rank(), survivedPieceInfo.file());
            Piece survivedPiece = survivedPieceInfo.piece();
            newBoard.put(position, survivedPiece);
        }
        return new ChessGame(currentTurnDao.find(), new ChessBoard(newBoard));
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

    public void saveGame(ChessGame chessGame) {
        saveSurvivedPieces(chessGame);
        saveCurrentTurn(chessGame);
    }

    private void saveSurvivedPieces(ChessGame chessGame) {
        Map<Position, Square> survivedPieces = chessGame.getSurvivedPieces();
        Set<PieceInfo> survivedPieceInfos = survivedPieces.entrySet().stream()
                .map(entry -> PieceInfo.of(
                        entry.getValue().getColor(),
                        entry.getKey().file(),
                        entry.getKey().rank(),
                        entry.getValue()))
                .collect(Collectors.toSet());
        pieceInfoDao.saveAll(survivedPieceInfos);
    }

    private void saveCurrentTurn(ChessGame chessGame) {
        currentTurnDao.save(chessGame.getCurrentTurn());
    }

    public void deleteSavedGame() {
        pieceInfoDao.deleteAll();
        currentTurnDao.deleteAll();
    }
}
