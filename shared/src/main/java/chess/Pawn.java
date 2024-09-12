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
      ChessPosition startMoveN = new ChessPosition(startPosition.getRow()+2, startPosition.getColumn());
      if (!N.isOutOfBounds() && !spaceOccupied(board, N)) {
        if (N.getRow() == 8) {
          possibleMoves.add(new ChessMove(startPosition, N, PieceType.QUEEN));
          possibleMoves.add(new ChessMove(startPosition, N, PieceType.ROOK));
          possibleMoves.add(new ChessMove(startPosition, N, PieceType.BISHOP));
          possibleMoves.add(new ChessMove(startPosition, N, PieceType.KNIGHT));
        }
        else {
          possibleMoves.add(new ChessMove(startPosition, N));
        }

      }
      if ((!NE.isOutOfBounds() && (spaceOccupied(board, NE))) && board.getPiece(NE).getTeamColor() == ChessGame.TeamColor.BLACK) {
        if (NE.getRow() == 8) {
          possibleMoves.add(new ChessMove(startPosition, NE, PieceType.QUEEN));
          possibleMoves.add(new ChessMove(startPosition, NE, PieceType.ROOK));
          possibleMoves.add(new ChessMove(startPosition, NE, PieceType.BISHOP));
          possibleMoves.add(new ChessMove(startPosition, NE, PieceType.KNIGHT));
        }
        else {
          possibleMoves.add(new ChessMove(startPosition, NE));
        }      }
      if ((!NW.isOutOfBounds() && (spaceOccupied(board, NW))) && board.getPiece(NW).getTeamColor() == ChessGame.TeamColor.BLACK) {
        if (N.getRow() == 8) {
          possibleMoves.add(new ChessMove(startPosition, N, PieceType.QUEEN));
          possibleMoves.add(new ChessMove(startPosition, N, PieceType.ROOK));
          possibleMoves.add(new ChessMove(startPosition, NW, PieceType.BISHOP));
          possibleMoves.add(new ChessMove(startPosition, NW, PieceType.KNIGHT));
        }
        else {
          possibleMoves.add(new ChessMove(startPosition, NW));
        }      }
      if (startPosition.getRow() == 2 && !spaceOccupied(board, N) && !spaceOccupied(board, startMoveN)) {
        possibleMoves.add(new ChessMove(startPosition, startMoveN));
      }
    }

    if (pieceColor == ChessGame.TeamColor.BLACK) {
      ChessPosition S = new ChessPosition(startPosition.getRow()-1, startPosition.getColumn());
      ChessPosition SE = new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+1);
      ChessPosition SW = new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-1);
      ChessPosition startMoveS = new ChessPosition(startPosition.getRow()-2, startPosition.getColumn());
      if (!S.isOutOfBounds() && !spaceOccupied(board, S)) {
        if (S.getRow() == 1) {
          possibleMoves.add(new ChessMove(startPosition, S, PieceType.QUEEN));
          possibleMoves.add(new ChessMove(startPosition, S, PieceType.ROOK));
          possibleMoves.add(new ChessMove(startPosition, S, PieceType.BISHOP));
          possibleMoves.add(new ChessMove(startPosition, S, PieceType.KNIGHT));
        }
        else {
          possibleMoves.add(new ChessMove(startPosition, S));
        }      }
      if ((!SE.isOutOfBounds() && (spaceOccupied(board, SE))) && board.getPiece(SE).getTeamColor() == ChessGame.TeamColor.WHITE) {
        if (SE.getRow() == 1) {
          possibleMoves.add(new ChessMove(startPosition, SE, PieceType.QUEEN));
          possibleMoves.add(new ChessMove(startPosition, SE, PieceType.ROOK));
          possibleMoves.add(new ChessMove(startPosition, SE, PieceType.BISHOP));
          possibleMoves.add(new ChessMove(startPosition, SE, PieceType.KNIGHT));
        }
        else {
          possibleMoves.add(new ChessMove(startPosition, SE));
        }      }
      if ((!SW.isOutOfBounds() && (spaceOccupied(board, SW))) && board.getPiece(SW).getTeamColor() == ChessGame.TeamColor.WHITE) {
        if (SW.getRow() == 1) {
          possibleMoves.add(new ChessMove(startPosition, SW, PieceType.QUEEN));
          possibleMoves.add(new ChessMove(startPosition, SW, PieceType.ROOK));
          possibleMoves.add(new ChessMove(startPosition, SW, PieceType.BISHOP));
          possibleMoves.add(new ChessMove(startPosition, SW, PieceType.KNIGHT));
        }
        else {
          possibleMoves.add(new ChessMove(startPosition, SW));
        }      }
      if (startPosition.getRow() == 7 && !spaceOccupied(board, S) && !spaceOccupied(board, startMoveS)) {
        possibleMoves.add(new ChessMove(startPosition, startMoveS));
      }
    }
    return possibleMoves;
  }
}
