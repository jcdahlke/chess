package chess;

import java.util.ArrayList;
import java.util.Collection;

public class King extends ChessPiece {
  private final PieceType type = PieceType.BISHOP;
  private final ChessPosition startPosition;
  private final Collection<ChessMove> possibleMoves;
  private final ChessBoard board;
  private final ChessGame.TeamColor pieceColor;

  public King(ChessGame.TeamColor pieceColor, PieceType type, ChessPosition startPosition, Collection<ChessMove> possibleMoves, ChessBoard board) {
    super(pieceColor, type);

    this.startPosition=startPosition;
    this.possibleMoves=possibleMoves;
    this.board = board;
    this.pieceColor = pieceColor;
  }

  public Collection<ChessMove> potentialMoveAdder() {
    ArrayList<ChessPosition> possibleKingMoves = new ArrayList<>();
    possibleKingMoves.add(new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+1));
    possibleKingMoves.add(new ChessPosition(startPosition.getRow(), startPosition.getColumn()+1));
    possibleKingMoves.add(new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+1));
    possibleKingMoves.add(new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()));
    possibleKingMoves.add(new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-1));
    possibleKingMoves.add(new ChessPosition(startPosition.getRow(), startPosition.getColumn()-1));
    possibleKingMoves.add(new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-1));
    possibleKingMoves.add(new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()));
    for (ChessPosition move:possibleKingMoves) {
      if(!move.isOutOfBounds() && board.getPiece(move) == null) {
        possibleMoves.add(new ChessMove(startPosition, move));
      }
      else if(!move.isOutOfBounds() && board.getPiece(move).getTeamColor() != pieceColor) {
        possibleMoves.add(new ChessMove(startPosition, move));
      }
    }
    return possibleMoves;
  }
}
