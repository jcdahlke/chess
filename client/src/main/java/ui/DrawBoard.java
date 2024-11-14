package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawBoard {
  private static final int BOARD_SIZE_IN_SQUARES = 8;
  private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
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
    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

    out.print(EMPTY.repeat(prefixLength));
    printHeaderText(out, headerText);
    out.print(EMPTY.repeat(suffixLength));
  }

  private void printHeaderText(PrintStream out, String text) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_GREEN);
    out.print(text);
    setBlack(out);
  }

  private void drawChessBoard(PrintStream out) {
    String[] rowLabels = displayFromWhitePerspective ? new String[]{"1", "2", "3", "4", "5", "6", "7", "8"}
            : new String[]{"8", "7", "6", "5", "4", "3", "2", "1"};
    int[] rowIndexes = displayFromWhitePerspective ? new int[]{0, 1, 2, 3, 4, 5, 6, 7}
            : new int[]{7, 6, 5, 4, 3, 2, 1, 0};

    for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
      drawRow(out, rowIndexes[i], rowLabels[i]);
    }
  }

  private void drawRow(PrintStream out, int rowIndex, String rowLabel) {
    for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; squareRow++) {
      if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
        out.print(" " + rowLabel + " "); // Side label
      } else {
        out.print("   ");
      }

      for (int colIndex = 0; colIndex < BOARD_SIZE_IN_SQUARES; colIndex++) {
        int col = displayFromWhitePerspective ? colIndex : 7 - colIndex;
        drawSquare(out, rowIndex, col);
      }
      out.println();
    }
  }

  private void drawSquare(PrintStream out, int row, int col) {
    ChessPiece piece = chessBoard.getPiece(new ChessPosition(row, col));
    boolean isDarkSquare = (row + col) % 2 == 1;

    if (isDarkSquare) {
      out.print(SET_BG_COLOR_BLACK);
    } else {
      out.print(SET_BG_COLOR_WHITE);
    }

    String pieceRepresentation = piece != null ? piece.toString() : " ";
    int paddingLength = (SQUARE_SIZE_IN_PADDED_CHARS - 1) / 2;

    out.print(EMPTY.repeat(paddingLength) + pieceRepresentation + EMPTY.repeat(paddingLength));
  }

  private void setBlack(PrintStream out) {
    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
  }
}
