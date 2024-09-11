package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Bishop extends ChessPiece {
  private final PieceType type = PieceType.BISHOP;
  private final ChessPosition startPosition;
  private final Collection<ChessMove> possibleMoves;

  public Bishop(ChessGame.TeamColor pieceColor, PieceType type, ChessPosition startPosition, Collection<ChessMove> possibleMoves) {
    super(pieceColor, type);

    this.startPosition=startPosition;
    this.possibleMoves=possibleMoves;
  }

  public Collection<ChessMove> potentialMoveAdder() {
    ChessPosition NE = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
    ChessPosition NW = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1);
    ChessPosition SE = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 1);
    ChessPosition SW = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1);
    while (!NE.isOutOfBounds()) {
      possibleMoves.add(new ChessMove(startPosition, NE));
      NE = new ChessPosition(NE.getRow() + 1, NE.getColumn() + 1);
    }

    while (!NW.isOutOfBounds()) {
      possibleMoves.add(new ChessMove(startPosition, NW));
      NW = new ChessPosition(NW.getRow() + 1, NW.getColumn() - 1);
    }

    while (!SE.isOutOfBounds()) {
      possibleMoves.add(new ChessMove(startPosition, SE));
      SE = new ChessPosition(SE.getRow() - 1, SE.getColumn() + 1);
    }

    while (!SW.isOutOfBounds()) {
      possibleMoves.add(new ChessMove(startPosition, SW));
      SW = new ChessPosition(SW.getRow() - 1, SW.getColumn() - 1);
    }

    return possibleMoves;
  }


}
