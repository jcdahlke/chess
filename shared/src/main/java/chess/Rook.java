package chess;

import java.util.ArrayList;
import java.util.Collection;

public class Rook extends ChessPiece {
  private final PieceType type=PieceType.BISHOP;
  private final ChessPosition startPosition;
  private final Collection<ChessMove> possibleMoves;
  private final ChessBoard board;
  private final ChessGame.TeamColor pieceColor;

  public Rook(ChessGame.TeamColor pieceColor, PieceType type, ChessPosition startPosition, Collection<ChessMove> possibleMoves, ChessBoard board) {
    super(pieceColor, type);

    this.startPosition=startPosition;
    this.possibleMoves=possibleMoves;
    this.board=board;
    this.pieceColor=pieceColor;
  }

  public Collection<ChessMove> potentialMoveAdder() {
    ChessPosition N = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn());
    ChessPosition E = new ChessPosition(startPosition.getRow(), startPosition.getColumn() + 1);
    ChessPosition S = new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn());
    ChessPosition W = new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 1);
    continualMoves(N,1,0);
    continualMoves(E,0,1);
    continualMoves(S,-1,0);
    continualMoves(W,0,-1);
    return possibleMoves;
  }

  public void continualMoves(ChessPosition direction, int rowIncrement, int columnIncrement) {
    while (!direction.isOutOfBounds() && (!spaceOccupied(board, direction))) {
      possibleMoves.add(new ChessMove(startPosition, direction));
      direction = new ChessPosition(direction.getRow() + rowIncrement, direction.getColumn() + columnIncrement);
    }
    if (!direction.isOutOfBounds() && board.getPiece(direction).getTeamColor() != pieceColor) {
      possibleMoves.add(new ChessMove(startPosition, direction));
    }
  }
}