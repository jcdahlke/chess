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
                        if (board.getPiece(move.getEndPosition())!=null &&
                                board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING) {
                            return true;
                        }
                    }
                }

            }
        }
        return false;
    }

    /**
     * Determines if the given team is in check given theoretical board
     *
     * @param teamColor which team to check for check
     * @param copyBoard a copy of the current playing board with changes
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor, ChessBoard copyBoard) {
        // Iterate through the board to see if any opponent piece can capture the King
        return canCaptureKing(teamColor, copyBoard);
    }

    private boolean canCaptureKing(TeamColor teamColor, ChessBoard copyBoard) {
        for (int r = 1; r < 9; r++) {
            for (int c = 1; c < 9; c++) {
                ChessPiece currentPiece = copyBoard.getPiece(new ChessPosition(r, c));
                if (isOpponentPiece(currentPiece, teamColor) && canPieceCaptureKing(currentPiece, copyBoard, new ChessPosition(r, c))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOpponentPiece(ChessPiece piece, TeamColor teamColor) {
        // Check if the piece belongs to the opponent's team
        return piece != null && piece.getTeamColor() != teamColor;
    }

    private boolean canPieceCaptureKing(ChessPiece piece, ChessBoard copyBoard, ChessPosition position) {
        // Get all possible moves for the piece and check if it can capture the King
        Collection<ChessMove> possibleMoves = piece.pieceMoves(copyBoard, position);
        return anyMoveCapturesKing(possibleMoves, copyBoard);
    }

    private boolean anyMoveCapturesKing(Collection<ChessMove> possibleMoves, ChessBoard copyBoard) {
        // Check if any move results in capturing the King
        for (ChessMove move : possibleMoves) {
            ChessPiece targetPiece = copyBoard.getPiece(move.getEndPosition());
            if (isKing(targetPiece)) {
                return true;
            }
        }
        return false;
    }

    private boolean isKing(ChessPiece piece) {
        // Check if the piece is a King
        return piece != null && piece.getPieceType() == ChessPiece.PieceType.KING;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // If the team is not in check, they cannot be in checkmate
        if (!isInCheck(teamColor)) {
            return false;
        }

        // Check if any piece of the given team can make a valid move
        return !hasAnyValidMove(teamColor);
    }

    private boolean hasAnyValidMove(TeamColor teamColor) {
        // Iterate over all positions on the board and check if there's a valid move
        for (int r = 1; r < 9; r++) {
            for (int c = 1; c < 9; c++) {
                ChessPiece currentPiece = board.getPiece(new ChessPosition(r, c));
                if (isTeamPiece(currentPiece, teamColor) && hasValidMove(new ChessPosition(r, c))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isTeamPiece(ChessPiece piece, TeamColor teamColor) {
        // Check if the piece belongs to the specified team
        return piece != null && piece.getTeamColor() == teamColor;
    }

    private boolean hasValidMove(ChessPosition position) {
        // Check if the piece at the given position has any valid moves
        Collection<ChessMove> possibleMoves = validMoves(position);
        return !possibleMoves.isEmpty();
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
