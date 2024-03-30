package chess.controller;

import chess.db.ChessBoardLoader;
import chess.db.CurrentTurnDao;
import chess.db.PieceInfo;
import chess.db.PieceInfoDao;
import chess.db.translator.ColorTranslator;
import chess.db.translator.FileTranslator;
import chess.db.translator.PieceTranslator;
import chess.db.translator.RankTranslator;
import chess.domain.ChessGame;
import chess.command.Command;
import chess.command.CommandType;
import chess.domain.CurrentTurn;
import chess.domain.board.ChessBoardMaker;
import chess.domain.position.Path;
import chess.domain.position.Position;
import chess.domain.square.Score;
import chess.domain.square.Square;
import chess.domain.square.piece.Color;
import chess.util.ExceptionRetryHandler;
import chess.view.InputView;
import chess.view.OutputView;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChessController {
    private final InputView inputView;
    private final OutputView outputView;

    public ChessController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        outputView.printStartMessage();
        Command firstCommand = ExceptionRetryHandler.handle(this::readFirstCommand);
        if (firstCommand.type() == CommandType.START) {
            startGame();
        }
    }

    private Command readFirstCommand() {
        Command command = inputView.readCommand();
        if (command.type() != CommandType.START && command.type() != CommandType.END) {
            throw new IllegalArgumentException("첫 커맨드는 start 또는 end만 가능합니다.");
        }
        return command;
    }

    private void startGame() {
        ChessGame chessGame = makeChessGame();
        outputView.printChessBoard(chessGame.getBoard());
        ExceptionRetryHandler.handle(() -> executeCommandsUntilEnd(chessGame));
    }

    private ChessGame makeChessGame() {
        PieceInfoDao pieceInfoDao = new PieceInfoDao();
        if (pieceInfoDao.isEmpty()) {
            ChessBoardMaker chessBoardMaker = new ChessBoardMaker();
            return new ChessGame(new CurrentTurn(Color.WHITE), chessBoardMaker.make());
        }
        ChessBoardLoader chessBoardLoader = ChessBoardLoader.from(pieceInfoDao.findAll());
        CurrentTurnDao currentTurnDao = new CurrentTurnDao();
        return new ChessGame(currentTurnDao.find(), chessBoardLoader.load());
    }

    private void executeCommandsUntilEnd(ChessGame chessGame) {
        Command command = inputView.readCommand();
        while (command.type() != CommandType.END) {
            executeCommand(command, chessGame);
            command = readCommandUnlessGameEnd(chessGame);
        }
        if (doesEnd(chessGame)) {
            outputView.printEndMessage(chessGame.findWinner());
            return;
        }
        saveGameOrNot(chessGame);
    }

    private void executeCommand(Command command, ChessGame chessGame) {
        if (command.type() == CommandType.START) {
            throw new IllegalArgumentException("이미 게임이 진행중이므로 start 외의 커맨드를 입력해 주세요.");
        }
        if (command.type() == CommandType.MOVE) {
            movePlayerPiece(command, chessGame);
            return;
        }
        if (command.type() == CommandType.STATUS) {
            executeStatusCommand(chessGame);
        }
    }

    private void movePlayerPiece(Command command, ChessGame chessGame) {
        Path path = new Path(command.getStartPosition(), command.getTargetPosition());
        chessGame.move(path);

        outputView.printChessBoard(chessGame.getBoard());
    }

    private void executeStatusCommand(ChessGame chessGame) {
        Score whiteScore = chessGame.calculateScore(Color.WHITE);
        Score blackScore = chessGame.calculateScore(Color.BLACK);
        Color leadingSide = whiteScore.findLeadingSide(blackScore);

        outputView.printStatus(whiteScore, blackScore, leadingSide);
    }

    private Command readCommandUnlessGameEnd(ChessGame chessGame) {
        if (doesEnd(chessGame)) {
            return Command.from(CommandType.END);
        }
        return inputView.readCommand();
    }

    private boolean doesEnd(ChessGame chessGame) {
        return chessGame.findWinner() != Color.NO_COLOR;
    }

    private void saveGameOrNot(ChessGame chessGame) {
        Command command = inputView.readCommandSaveOrNot();
        if (command.type() == CommandType.SAVE) {
            saveGame(chessGame);
            return;
        }
        deleteSavedGame();
    }

    private void saveGame(ChessGame chessGame) {
        saveSurvivedPieces(chessGame);
        saveCurrentTurn(chessGame);
    }

    private void saveSurvivedPieces(ChessGame chessGame) {
        Map<Position, Square> survivedPieces = chessGame.getSurvivedPieces();
        Set<PieceInfo> survivedPieceInfos = survivedPieces.entrySet().stream()
                .map(entry -> new PieceInfo(
                        ColorTranslator.translate(entry.getValue().getColor()),
                        FileTranslator.translate(entry.getKey().file()),
                        RankTranslator.translate(entry.getKey().rank()),
                        PieceTranslator.translate(entry.getValue())))
                .collect(Collectors.toSet());
        PieceInfoDao pieceInfoDao = new PieceInfoDao();
        pieceInfoDao.deleteAll();
        pieceInfoDao.saveAll(survivedPieceInfos);
    }

    private void saveCurrentTurn(ChessGame chessGame) {
        CurrentTurnDao currentTurnDao = new CurrentTurnDao();
        currentTurnDao.save(chessGame.getCurrentTurn());
    }

    private void deleteSavedGame() {
        new PieceInfoDao().deleteAll();
        new CurrentTurnDao().deleteAll();
    }
}
