package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawBoard {
  private static final int BOARD_SIZE_IN_SQUARES = 8;
  private static final int SQUARE_SIZE_IN_PADDED_CHARS = 4;
  private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;
  private static final String EMPTY = "   ";

  private final ChessBoard chessBoard;
  private final boolean displayFromWhitePerspective; // If true, display from white's perspective

  public DrawBoard(ChessBoard chessBoard, String color) {
    this.chessBoard = chessBoard;
    this.displayFromWhitePerspective = color.equalsIgnoreCase("white");
  }

  public DrawBoard(ChessBoard chessBoard) {
    this(chessBoard, "white"); // Default to white's perspective
  }

  public void displayBoard() {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    out.print(ERASE_SCREEN);
    drawHeaders(out);
    drawChessBoard(out);
    drawHeaders(out);
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  private void drawHeaders(PrintStream out) {
    setBlack(out);
    String[] headers = displayFromWhitePerspective ? new String[]{"a", "b", "c", "d", "e", "f", "g", "h"}
            : new String[]{"h", "g", "f", "e", "d", "c", "b", "a"};

    out.print("   "); // Spacer before column labels
    for (String header : headers) {
      drawHeader(out, header);
      out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
    }
    out.println();
  }

  private void drawHeader(PrintStream out, String headerText) {
    int prefixLength = 1;
    int suffixLength = 0;

    out.print(EMPTY.repeat(prefixLength));
    printHeaderText(out, headerText);
    out.print(EMPTY.repeat(suffixLength));
  }

  private void printHeaderText(PrintStream out, String text) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
    out.print(text);
    setBlack(out);
  }

  private void drawChessBoard(PrintStream out) {
    String[] rowLabels = displayFromWhitePerspective ? new String[]{"8", "7", "6", "5", "4", "3", "2", "1"}
            : new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
    int[] rowIndexes = displayFromWhitePerspective ? new int[]{7, 6, 5, 4, 3, 2, 1, 0}
            : new int[]{0, 1, 2, 3, 4, 5, 6, 7};

    for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
      drawRow(out, rowIndexes[i], rowLabels[i]);
    }
  }

  private void drawRow(PrintStream out, int rowIndex, String rowLabel) {
    out.print(" " + rowLabel + " ");  // Print the row label at the start of the row

    // Now print each square in the row, only once
    for (int colIndex = 0; colIndex < BOARD_SIZE_IN_SQUARES; colIndex++) {
      int col = displayFromWhitePerspective ? colIndex : 7 - colIndex;

      // Debugging: check column index for valid bounds
      if (col < 0 || col >= BOARD_SIZE_IN_SQUARES) {
        System.out.println("Error: Invalid column index " + col + " for row " + rowIndex);
        continue;  // Skip drawing this square if the index is invalid
      }

      drawSquare(out, rowIndex, col);  // Draw the square for this column
    }
    out.print(" " + rowLabel + " ");  // Print the row label at the end of the row
    out.println();  // Move to the next line after each row
  }

  private void drawSquare(PrintStream out, int row, int col) {
    ChessPiece piece = chessBoard.getPiece(new ChessPosition(row + 1, col + 1));
    boolean isDarkSquare = (row + col) % 2 == 1;

    // Choose background color based on square color
    if (isDarkSquare) {
      out.print(SET_BG_COLOR_BLACK);
    } else {
      out.print(SET_BG_COLOR_LIGHT_GREY);
    }

    // Get the Unicode character for the piece (or space if empty)
    String pieceRepresentation = piece != null ? getUnicodeForPiece(piece) : " ";

    // Set the text color based on the team color
    if (piece != null) {
      if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
        out.print(SET_TEXT_COLOR_BLUE);  // Blue for white team pieces
      } else {
        out.print(SET_TEXT_COLOR_RED);   // Red for black team pieces
      }
    }

    // Calculate padding for centering the piece
    int paddingLength = (SQUARE_SIZE_IN_PADDED_CHARS - pieceRepresentation.length()) / 2;

    // Print padding and the piece, only once per square
    out.print(EMPTY.repeat(paddingLength) + pieceRepresentation + EMPTY.repeat(paddingLength));

    // Reset the background color and text color for the next square
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
  }



  private void setBlack(PrintStream out) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
  }

  private String getUnicodeForPiece(ChessPiece piece) {
    switch (piece.getPieceType()) {
      case KING:
        return "K";
      case QUEEN:
        return "Q";
      case BISHOP:
        return "B";
      case KNIGHT:
        return "N";
      case ROOK:
        return "R";
      case PAWN:
        return "P";
      default:
        return " ";
    }
  }

}
