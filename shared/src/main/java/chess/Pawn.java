package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Pawn extends ChessPiece {
  private final PieceType type = PieceType.PAWN;
  private final ChessPosition startPosition;
  private final Collection<ChessMove> possibleMoves;
  private final ChessBoard board;
  private final ChessGame.TeamColor pieceColor;

  public Pawn(ChessGame.TeamColor pieceColor, PieceType type, ChessPosition startPosition, Collection<ChessMove> possibleMoves, ChessBoard board) {
    super(pieceColor, type);

    this.startPosition=startPosition;
    this.possibleMoves=possibleMoves;
    this.board = board;
    this.pieceColor = pieceColor;
  }

  public Collection<ChessMove> potentialMoveAdder() {
    if (pieceColor == ChessGame.TeamColor.WHITE) {
      ChessPosition N = new ChessPosition(startPosition.getRow()+1, startPosition.getColumn());
      ChessPosition NE = new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+1);
      ChessPosition NW = new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-1);
      if (!N.isOutOfBounds() && !spaceOccupied(board, N)) {
        possibleMoves.add(new ChessMove(startPosition, N));
      }
      if ((!NE.isOutOfBounds() && (spaceOccupied(board, NE))) && board.getPiece(NE).getTeamColor() == ChessGame.TeamColor.BLACK) {
        possibleMoves.add(new ChessMove(startPosition, NE));
      }
      if ((!NW.isOutOfBounds() && (spaceOccupied(board, NW))) && board.getPiece(NW).getTeamColor() == ChessGame.TeamColor.BLACK) {
        possibleMoves.add(new ChessMove(startPosition, NW));
      }
    }

    if (pieceColor == ChessGame.TeamColor.BLACK) {
      ChessPosition S = new ChessPosition(startPosition.getRow()-1, startPosition.getColumn());
      ChessPosition SE = new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+1);
      ChessPosition SW = new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-1);
      if (!S.isOutOfBounds() && !spaceOccupied(board, S)) {
        possibleMoves.add(new ChessMove(startPosition, S));
      }
      if ((!SE.isOutOfBounds() && (spaceOccupied(board, SE))) && board.getPiece(SE).getTeamColor() == ChessGame.TeamColor.WHITE) {
        possibleMoves.add(new ChessMove(startPosition, SE));
      }
      if ((!SW.isOutOfBounds() && (spaceOccupied(board, SW))) && board.getPiece(SW).getTeamColor() == ChessGame.TeamColor.WHITE) {
        possibleMoves.add(new ChessMove(startPosition, SW));
      }
    }
    return possibleMoves;
  }
}
