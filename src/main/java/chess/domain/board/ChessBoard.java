package chess.domain.board;

import chess.domain.position.Path;
import chess.domain.position.Position;
import chess.domain.square.Empty;
import chess.domain.square.Score;
import chess.domain.square.Square;
import chess.domain.square.piece.Color;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChessBoard {
    private final Map<Position, Square> squares;

    public ChessBoard(Map<Position, Square> squares) {
        this.squares = new HashMap<>(squares);
    }

    public void move(Path path) {
        Square startSquare = squares.get(path.startPosition());
        validateCanMove(startSquare, path);

        squares.put(path.targetPosition(), startSquare);
        squares.put(path.startPosition(), Empty.getInstance());
    }

    private void validateCanMove(Square startSquare, Path path) {
        if (!startSquare.canArrive(path, squares)) {
            throw new IllegalArgumentException("해당 위치로 움직일 수 없습니다.");
        }
    }

    public Score calculateScore(Color color) {
        double sum = squares.keySet().stream()
                .filter(position -> findSquare(position).isColor(color))
                .mapToDouble(position -> findSquare(position).score(sameFileSquares(position)).getValue())
                .sum();

        return Score.of(sum, color);
    }

    private Set<Square> sameFileSquares(Position position) {
        return position.sameFilePositions()
                .stream()
                .map(this::findSquare)
                .collect(Collectors.toSet());
    }

    public Square findSquare(Position position) {
        return squares.get(position);
    }

    public Map<Position, Square> getSquares() {
        return Collections.unmodifiableMap(squares);
    }
}
