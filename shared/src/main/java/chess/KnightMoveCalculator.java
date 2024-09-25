package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalculator extends PieceMoveCalculator {
  public KnightMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition position) {
    super(piece, board, position);
  }

  public Collection<ChessMove> getPossibleMoves() {
    Collection<ChessMove> possibleMoves = new ArrayList<>();
    possibleMoves.addAll(directionMover(2,1,false));
    possibleMoves.addAll(directionMover(1,2,false));
    possibleMoves.addAll(directionMover(-1,2,false));
    possibleMoves.addAll(directionMover(-2,1,false));
    possibleMoves.addAll(directionMover(-2,-1,false));
    possibleMoves.addAll(directionMover(-1,-2,false));
    possibleMoves.addAll(directionMover(1,-2,false));
    possibleMoves.addAll(directionMover(2,-1,false));
    return possibleMoves;
  }
}
