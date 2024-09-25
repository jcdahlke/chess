package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalculator extends PieceMoveCalculator {

  public PawnMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition position) {
    super(piece, board, position);
  }


  public Collection<ChessMove> getPossibleMoves() {
    Collection<ChessMove> possibleMoves = new ArrayList<>();
    if (getPiece().getTeamColor() == ChessGame.TeamColor.WHITE) {
      possibleMoves.addAll(directionMover(1,0,false));
      possibleMoves.addAll(directionMover(1,1,true));
      possibleMoves.addAll(directionMover(1,-1,true));
      possibleMoves.addAll(directionMover(2,0,false));
    }
    else {
      possibleMoves.addAll(directionMover(-1,0,false));
      possibleMoves.addAll(directionMover(-1,1,true));
      possibleMoves.addAll(directionMover(-1,-1,true));
      possibleMoves.addAll(directionMover(-2,0,false));
    }
    return possibleMoves;
  }
  @Override
  public Collection<ChessMove> directionMover(int rowIncrement, int colIncrement, boolean diagonalMovement) {
    Collection<ChessMove> possibleMoves = new ArrayList<>();
    ChessPosition newPosition=  new ChessPosition(getPosition().getRow()+rowIncrement, getPosition().getColumn()+colIncrement);
    if (diagonalMovement) {
      if (!newPosition.isOutOfBounds() && spaceOccupied(newPosition) && getBoard().getPiece(newPosition).getTeamColor() != getPiece().getTeamColor()) {

        if (newPosition.getRow() == 8 && getPiece().getTeamColor() == ChessGame.TeamColor.WHITE) {
          possibleMoves.addAll(allPromotionTypes(getPosition(),newPosition));
        }
        else if (newPosition.getRow() == 1 && getPiece().getTeamColor() == ChessGame.TeamColor.BLACK) {
          possibleMoves.addAll(allPromotionTypes(getPosition(),newPosition));
        }
        else {
          possibleMoves.add(new ChessMove(getPosition(), newPosition));
        }
      }

    }
    else {
      if (getPosition().getRow() == 2 && getPiece().getTeamColor() == ChessGame.TeamColor.WHITE && rowIncrement == 2) {
        if (!spaceOccupied(newPosition) && !spaceOccupied(new ChessPosition(getPosition().getRow()+1, getPosition().getColumn()))) {
          possibleMoves.add(new ChessMove(getPosition(), newPosition));
        }
      }
      if (getPosition().getRow() == 7 && getPiece().getTeamColor() == ChessGame.TeamColor.BLACK && rowIncrement == -2) {
        if (!spaceOccupied(newPosition) && !spaceOccupied(new ChessPosition(getPosition().getRow()-1, getPosition().getColumn()))) {
          possibleMoves.add(new ChessMove(getPosition(), newPosition));
        }
      }
      if (!newPosition.isOutOfBounds() && !spaceOccupied(newPosition) && rowIncrement != 2 && rowIncrement != -2) {
        if (newPosition.getRow() == 8 && getPiece().getTeamColor() == ChessGame.TeamColor.WHITE) {
          possibleMoves.addAll(allPromotionTypes(getPosition(),newPosition));
        }
        else if (newPosition.getRow() == 1 && getPiece().getTeamColor() == ChessGame.TeamColor.BLACK) {
          possibleMoves.addAll(allPromotionTypes(getPosition(),newPosition));
        }
        else {
          possibleMoves.add(new ChessMove(getPosition(), newPosition));
        }
      }
    }


    return possibleMoves;
  }

  public Collection<ChessMove> allPromotionTypes(ChessPosition startMove, ChessPosition endMove) {
    Collection<ChessMove> promotionPieces = new ArrayList<>();
    promotionPieces.add(new ChessMove(startMove,endMove, ChessPiece.PieceType.BISHOP));
    promotionPieces.add(new ChessMove(startMove,endMove, ChessPiece.PieceType.QUEEN));
    promotionPieces.add(new ChessMove(startMove,endMove, ChessPiece.PieceType.KNIGHT));
    promotionPieces.add(new ChessMove(startMove,endMove, ChessPiece.PieceType.ROOK));
    return promotionPieces;
  }

}