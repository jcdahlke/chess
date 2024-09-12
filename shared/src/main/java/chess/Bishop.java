package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Bishop extends ChessPiece {
  private final PieceType type = PieceType.BISHOP;
  private final ChessPosition startPosition;
  private final Collection<ChessMove> possibleMoves;
  private final ChessBoard board;
  private final ChessGame.TeamColor pieceColor;

  public Bishop(ChessGame.TeamColor pieceColor, PieceType type, ChessPosition startPosition, Collection<ChessMove> possibleMoves, ChessBoard board) {
    super(pieceColor, type);

    this.startPosition=startPosition;
    this.possibleMoves=possibleMoves;
    this.board = board;
    this.pieceColor = pieceColor;
  }

  public Collection<ChessMove> potentialMoveAdder() {
    ChessPosition NE = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
    ChessPosition NW = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);
    ChessPosition SE = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 1);
    ChessPosition SW = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);
    while ((!NE.isOutOfBounds()) && (!spaceOccupied(board, NE))) {
      possibleMoves.add(new ChessMove(startPosition, NE));
      NE = new ChessPosition(NE.getRow() + 1, NE.getColumn() + 1);
    }
    if ((!NE.isOutOfBounds()) && (board.getPiece(NE).getTeamColor() != this.pieceColor)) {
      possibleMoves.add(new ChessMove(startPosition, NE));
    }

    while (!NW.isOutOfBounds() && !spaceOccupied(board, NW)) {
      possibleMoves.add(new ChessMove(startPosition, NW));
      NW = new ChessPosition(NW.getRow() + 1, NW.getColumn() - 1);
    }
    if (!NW.isOutOfBounds() && board.getPiece(NW).getTeamColor() != this.pieceColor) {
      possibleMoves.add(new ChessMove(startPosition, NW));
    }

    while (!SE.isOutOfBounds() && !spaceOccupied(board, SE)) {
      possibleMoves.add(new ChessMove(startPosition, SE));
      SE = new ChessPosition(SE.getRow() - 1, SE.getColumn() + 1);
    }
    if (!SE.isOutOfBounds() && board.getPiece(SE).getTeamColor() != this.pieceColor) {
      possibleMoves.add(new ChessMove(startPosition, SE));
    }

    while (!SW.isOutOfBounds() && !spaceOccupied(board, SW)) {
      possibleMoves.add(new ChessMove(startPosition, SW));
      SW = new ChessPosition(SW.getRow() - 1, SW.getColumn() - 1);
    }
    if (!SW.isOutOfBounds() && board.getPiece(SW).getTeamColor() != this.pieceColor) {
      possibleMoves.add(new ChessMove(startPosition, SW));
    }


    return possibleMoves;
  }


}
