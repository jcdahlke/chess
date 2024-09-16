package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor=pieceColor;
        this.type=type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {

        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moveCollection = new ArrayList<>();
        if (this.type == PieceType.BISHOP) {
            Bishop bishop = new Bishop(pieceColor, type, myPosition, moveCollection, board);
            bishop.potentialMoveAdder();
        }
        else if(this.type == PieceType.KING) {
            King king = new King(pieceColor, type, myPosition, moveCollection, board);
            king.potentialMoveAdder();
        }
        else if(this.type == PieceType.KNIGHT) {
            Knight knight = new Knight(pieceColor, type, myPosition, moveCollection, board);
            knight.potentialMoveAdder();
        }
        else if(this.type == PieceType.PAWN) {
            Pawn pawn = new Pawn(pieceColor, type, myPosition, moveCollection, board);
            pawn.potentialMoveAdder();
        }
        else if(this.type == PieceType.QUEEN) {
            Queen queen = new Queen(pieceColor, type, myPosition, moveCollection, board);
            queen.potentialMoveAdder();
        }
        else if(this.type == PieceType.ROOK) {
            Rook rook = new Rook(pieceColor, type, myPosition, moveCollection, board);
            rook.potentialMoveAdder();
        }
        return moveCollection;
    }

    public boolean spaceOccupied(ChessBoard board, ChessPosition position) {
        if (board.getPiece(position) == null) {
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public String toString() {

        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that=(ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
