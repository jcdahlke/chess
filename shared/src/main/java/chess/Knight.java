package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Knight extends ChessPiece {
  private final PieceType type = PieceType.KNIGHT;
  private final ChessPosition startPosition;
  private final Collection<ChessMove> possibleMoves;
  private final ChessBoard board;
  private final ChessGame.TeamColor pieceColor;

  public Knight(ChessGame.TeamColor pieceColor, PieceType type, ChessPosition startPosition, Collection<ChessMove> possibleMoves, ChessBoard board) {
    super(pieceColor, type);

    this.startPosition=startPosition;
    this.possibleMoves=possibleMoves;
    this.board = board;
    this.pieceColor = pieceColor;
  }

  public Collection<ChessMove> potentialMoveAdder() {
    ArrayList<ChessPosition> possibleKnightMoves=new ArrayList<>();
    possibleKnightMoves.add(new ChessPosition(startPosition.getRow()+2, startPosition.getColumn()+1));
    possibleKnightMoves.add(new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+2));
    possibleKnightMoves.add(new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+2));
    possibleKnightMoves.add(new ChessPosition(startPosition.getRow()-2, startPosition.getColumn()+1));
    possibleKnightMoves.add(new ChessPosition(startPosition.getRow()-2, startPosition.getColumn()-1));
    possibleKnightMoves.add(new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-2));
    possibleKnightMoves.add(new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-2));
    possibleKnightMoves.add(new ChessPosition(startPosition.getRow()+2, startPosition.getColumn()-1));
    for (ChessPosition move:possibleKnightMoves) {
      if(!move.isOutOfBounds() && !spaceOccupied(board, move)) {
        possibleMoves.add(new ChessMove(startPosition, move));
      }
      else if(!move.isOutOfBounds() && board.getPiece(move).getTeamColor() != pieceColor) {
        possibleMoves.add(new ChessMove(startPosition, move));
      }
    }
    return possibleMoves;
  }
}
