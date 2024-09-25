package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalculator extends PieceMoveCalculator {
  public RookMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition position) {
    super(piece, board, position);
  }

  public Collection<ChessMove> getPossibleMoves() {
    Collection<ChessMove> possibleMoves = new ArrayList<>();
    possibleMoves.addAll(directionMover(1,0,true));
    possibleMoves.addAll(directionMover(0,1,true));
    possibleMoves.addAll(directionMover(-1,0,true));
    possibleMoves.addAll(directionMover(0,-1,true));
    return possibleMoves;
  }
}
