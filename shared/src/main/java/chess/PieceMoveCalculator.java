package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMoveCalculator {
  private final ChessPiece piece;
  private final ChessBoard board;
  private final ChessPosition position;

  public PieceMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition position) {

    this.piece=piece;
    this.board=board;
    this.position=position;
  }

  public Collection<ChessMove> pieceSorter() {
    if (piece.getPieceType()== ChessPiece.PieceType.BISHOP) {
      BishopMoveCalculator bishop = new BishopMoveCalculator(piece,board,position);
      return bishop.getPossibleMoves();
    }
    else if (piece.getPieceType()== ChessPiece.PieceType.KING) {
      KingMoveCalculator king = new KingMoveCalculator(piece,board,position);
      return king.getPossibleMoves();
    }
    else if (piece.getPieceType()== ChessPiece.PieceType.QUEEN) {
      QueenMoveCalculator queen = new QueenMoveCalculator(piece,board,position);
      return queen.getPossibleMoves();
    }
    else if (piece.getPieceType()== ChessPiece.PieceType.ROOK) {
      RookMoveCalculator rook = new RookMoveCalculator(piece,board,position);
      return rook.getPossibleMoves();
    }
    else if (piece.getPieceType()== ChessPiece.PieceType.KNIGHT) {
      KnightMoveCalculator knight = new KnightMoveCalculator(piece,board,position);
      return knight.getPossibleMoves();
    }
    else if (piece.getPieceType()== ChessPiece.PieceType.PAWN) {
      PawnMoveCalculator pawn = new PawnMoveCalculator(piece,board,position);
      return pawn.getPossibleMoves();
    }
    else{
      return null;
    }
  }

  public ChessPiece getPiece() {
    return piece;
  }

  public ChessBoard getBoard() {
    return board;
  }

  public ChessPosition getPosition() {
    return position;
  }

  public Collection<ChessMove> directionMover(int rowIncrement, int columnIncrement, boolean continuousMovement) {
    Collection<ChessMove> chessMoveCollection = new ArrayList<>();
    ChessPosition newPosition = new ChessPosition(position.getRow() + rowIncrement, position.getColumn()+columnIncrement);
    if (continuousMovement){
      while (!newPosition.isOutOfBounds() && (!spaceOccupied(newPosition))) {
        chessMoveCollection.add(new ChessMove(position, newPosition));
        newPosition = new ChessPosition(newPosition.getRow() + rowIncrement, newPosition.getColumn()+columnIncrement);
      }
      if (!newPosition.isOutOfBounds() && (!spaceOccupied(newPosition) || board.getPiece(newPosition).getTeamColor() != piece.getTeamColor())) {
        chessMoveCollection.add(new ChessMove(position, newPosition));
      }
    }
    else {
      if (!newPosition.isOutOfBounds()) {
        if (!spaceOccupied(newPosition) || board.getPiece(newPosition).getTeamColor() != piece.getTeamColor()) {
          chessMoveCollection.add(new ChessMove(position, newPosition));
        }
      }
    }
    return chessMoveCollection;
  }


  public boolean spaceOccupied (ChessPosition position) {
    return board.getPiece(position) != null;
  }

}
