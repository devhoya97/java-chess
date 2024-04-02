package chess.service;

import chess.db.*;
import chess.db.dao.CurrentTurnDao;
import chess.db.dao.PieceInfoDao;
import chess.domain.ChessGame;
import chess.domain.CurrentTurn;
import chess.domain.board.ChessBoardMaker;
import chess.domain.position.Position;
import chess.domain.square.Square;
import chess.domain.square.piece.Color;

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
            ChessBoardMaker chessBoardMaker = new ChessBoardMaker();
            return new ChessGame(new CurrentTurn(Color.WHITE), chessBoardMaker.make());
        }
        ChessBoardLoader chessBoardLoader = ChessBoardLoader.from(pieceInfoDao.findAll());
        return new ChessGame(currentTurnDao.find(), chessBoardLoader.load());
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
