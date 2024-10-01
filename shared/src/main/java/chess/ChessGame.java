package chess;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor currentTeamTurn;
    private ChessBoard board;
    public ChessGame() {
        currentTeamTurn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {

        currentTeamTurn = team;

    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (board.getPiece(startPosition) == null) {
            return null;
        }
        else {
            Collection<ChessMove> moves = board.getPiece(startPosition).pieceMoves(board,startPosition);
            ChessBoard copyBoard = board.copy();
            Collection<ChessMove> copyMoves = new CopyOnWriteArrayList<>(moves);
            for (ChessMove move: copyMoves) {
                copyBoard.movePiece(move);
                if (isInCheck(copyBoard.getPiece(move.getEndPosition()).getTeamColor(),copyBoard)) {
                    moves.remove(move);
                }
                copyBoard = board.copy();
            }
            return moves;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> possibleMoves = validMoves(move.getStartPosition());
        if (possibleMoves == null){
            throw new InvalidMoveException("No piece at this position");
        }
        if(board.getPiece(move.getStartPosition()).getTeamColor() != currentTeamTurn) {
            throw new InvalidMoveException("It is not this teams turn");
        }

        if (possibleMoves.contains(move)) {
            board.movePiece(move);
            if (board.getPiece(move.getEndPosition()).getTeamColor() == TeamColor.WHITE) {
                currentTeamTurn = TeamColor.BLACK;
            }
            else {
                currentTeamTurn = TeamColor.WHITE;
            }
        }
        else {
            throw new InvalidMoveException("This is not a valid move");
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> possibleMoves;
        ChessPiece currentPiece;
        for (int r=1; r <9; r++) {
            for (int c = 1; c < 9; c++) {
                currentPiece = board.getPiece(new ChessPosition(r,c));
                if(currentPiece!=null && currentPiece.getTeamColor() != teamColor) {
                    possibleMoves = validMoves(new ChessPosition(r,c));
                    for (ChessMove move:possibleMoves) {
                        if (board.getPiece(move.getEndPosition())!=null && board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                            return true;
                        }
                    }
                }

            }
        }
        return false;
    }

    public boolean isInCheck(TeamColor teamColor, ChessBoard copyBoard) {
        Collection<ChessMove> possibleMoves;
        ChessPiece currentPiece;
        for (int r=1; r <9; r++) {
            for (int c = 1; c < 9; c++) {
                currentPiece =copyBoard.getPiece(new ChessPosition(r,c));
                if(currentPiece!=null && currentPiece.getTeamColor() != teamColor) {
                    possibleMoves =currentPiece.pieceMoves(copyBoard,new ChessPosition(r,c));
                    for (ChessMove move:possibleMoves) {
                        if(copyBoard.getPiece(move.getEndPosition())!=null && copyBoard.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                            return true;
                        }
                    }
                }

            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        Collection<ChessMove> possibleMoves;
        ChessPiece currentPiece;
        for (int r=1; r <9; r++) {
            for (int c = 1; c < 9; c++) {
                currentPiece =board.getPiece(new ChessPosition(r,c));
                if(currentPiece!=null && currentPiece.getTeamColor() == teamColor) {
                    possibleMoves = validMoves(new ChessPosition(r,c));
                    if (!possibleMoves.isEmpty()) {
                        return false;
                    }
                }

            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        Collection<ChessMove> possibleMoves;
        ChessPiece currentPiece;
        for (int r=1; r <9; r++) {
            for (int c = 1; c < 9; c++) {
                currentPiece =board.getPiece(new ChessPosition(r,c));
                if(currentPiece!=null && currentPiece.getTeamColor() == teamColor) {
                    possibleMoves = validMoves(new ChessPosition(r,c));
                    if (!possibleMoves.isEmpty()) {
                        return false;
                    }
                }

            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {

        return board;
    }
}
