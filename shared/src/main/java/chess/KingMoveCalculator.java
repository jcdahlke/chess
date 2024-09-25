package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveCalculator extends PieceMoveCalculator {
  public KingMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition position) {
    super(piece, board, position);
  }

  public Collection<ChessMove> getPossibleMoves() {
    Collection<ChessMove> possibleMoves=new ArrayList<>();
    possibleMoves.addAll(directionMover(1, 1, false));
    possibleMoves.addAll(directionMover(-1, 1, false));
    possibleMoves.addAll(directionMover(-1, -1, false));
    possibleMoves.addAll(directionMover(1, -1, false));
    possibleMoves.addAll(directionMover(1, 0, false));
    possibleMoves.addAll(directionMover(0, 1, false));
    possibleMoves.addAll(directionMover(-1, 0, false));
    possibleMoves.addAll(directionMover(0, -1, false));
    return possibleMoves;
  }
}