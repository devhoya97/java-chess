package chess.controller;

import chess.db.CurrentTurnDao;
import chess.db.PieceInfoDao;
import chess.domain.ChessGame;
import chess.command.Command;
import chess.command.CommandType;
import chess.domain.position.Path;
import chess.domain.square.Score;
import chess.domain.square.piece.Color;
import chess.service.ChessService;
import chess.util.ExceptionRetryHandler;
import chess.view.InputView;
import chess.view.OutputView;

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
        ChessService chessService = new ChessService(new PieceInfoDao(), new CurrentTurnDao());
        ChessGame chessGame = chessService.loadGame();
        outputView.printChessBoard(chessGame.getBoard());
        outputView.printTurnMessage(chessGame.getCurrentTurn());
        ExceptionRetryHandler.handle(() -> PlayGameUntilEnd(chessGame, chessService));
    }

    private void PlayGameUntilEnd(ChessGame chessGame, ChessService chessService) {
        Command command = inputView.readCommand();
        while (command.type() != CommandType.END) {
            runMoveOrStatus(command, chessGame);
            command = readCommandUnlessKingDied(chessGame);
        }
        chessService.deleteSavedGame();
        if (doesEnd(chessGame)) {
            outputView.printEndMessage(chessGame.findWinner());
            return;
        }
        ExceptionRetryHandler.handle(() -> saveGameOrNot(chessGame, chessService));
    }

    private void runMoveOrStatus(Command command, ChessGame chessGame) {
        if (command.type() == CommandType.MOVE) {
            movePlayerPiece(command, chessGame);
            return;
        }
        if (command.type() == CommandType.STATUS) {
            runStatusCommand(chessGame);
            return;
        }
        throw new IllegalArgumentException("게임 진행 중에는 move 또는 status 명령만 입력 가능합니다.");
    }

    private void movePlayerPiece(Command command, ChessGame chessGame) {
        Path path = new Path(command.getStartPosition(), command.getTargetPosition());
        chessGame.move(path);

        outputView.printChessBoard(chessGame.getBoard());
    }

    private void runStatusCommand(ChessGame chessGame) {
        Score whiteScore = chessGame.calculateScore(Color.WHITE);
        Score blackScore = chessGame.calculateScore(Color.BLACK);
        Color leadingSide = whiteScore.findLeadingSide(blackScore);

        outputView.printStatus(whiteScore, blackScore, leadingSide);
    }

    private Command readCommandUnlessKingDied(ChessGame chessGame) {
        if (doesEnd(chessGame)) {
            return Command.from(CommandType.END);
        }
        return inputView.readCommand();
    }

    private boolean doesEnd(ChessGame chessGame) {
        return chessGame.findWinner() != Color.NO_COLOR;
    }

    private void saveGameOrNot(ChessGame chessGame, ChessService chessService) {
        Command command = inputView.readCommandSaveOrEnd();
        if (command.type() == CommandType.SAVE) {
            chessService.saveGame(chessGame);
            return;
        }
        chessService.deleteSavedGame();
    }
}
