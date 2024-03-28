package chess.domain;

import chess.domain.square.piece.Color;

public class CurrentTurn {
    private final Color currentTurn;

    public CurrentTurn(Color startTurn) {
        this.currentTurn = startTurn;
    }

    public CurrentTurn change() {
        return new CurrentTurn(currentTurn.opposite());
    }

    public Color value() {
        return currentTurn;
    }
}
