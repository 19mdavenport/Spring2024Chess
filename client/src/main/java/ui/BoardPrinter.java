package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import data.DataCache;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BoardPrinter {
    public static void printGame(ChessGame game) {
        printGame(game, null, new HashSet<>());
    }

    public static void printGame(ChessGame game, ChessGame previous) {
        printGame(game, null, determineDifferences(game, previous));
    }

    public static void printGame(ChessGame game, ChessPosition highlight) {
        printGame(game, highlight, new HashSet<>());
    }

    private static void printGame(ChessGame game, ChessPosition position, Collection<ChessPosition> differences) {
        System.out.println();
        ChessGame.TeamColor perspective = DataCache.getInstance().getPlayerColor();
        if (perspective == null) {
            perspective = ChessGame.TeamColor.WHITE;
        }
        ChessBoardColorScheme colorScheme = DataCache.getInstance().getColorScheme();
        printHeader(colorScheme, perspective);

        Set<ChessPosition> highlight = (position != null && game.getBoard().getPiece(position) != null) ?
                game.validMoves(position).stream().map(move -> move.getEndPosition()).collect(Collectors.toSet()) :
                new HashSet<>();

        for (int i = 1; i <= 8; i++) {
            int row = (perspective == ChessGame.TeamColor.WHITE) ? 9 - i : i;

            System.out.print(colorScheme.getColorEscapeSequence(ChessBoardColorScheme.ColorType.BORDER_TEXT));
            System.out.print(colorScheme.getColorEscapeSequence(ChessBoardColorScheme.ColorType.BORDER));
            System.out.print(" " + row + ' ');

            for (int j = 1; j <= 8; j++) {
                int col = (perspective == ChessGame.TeamColor.BLACK) ? 9 - j : j;

                ChessPosition pos = new ChessPosition(row, col);

                boolean lightSquare = (pos.getRow() + pos.getColumn()) % 2 == 1;

                ChessBoardColorScheme.ColorType type = (lightSquare) ? ChessBoardColorScheme.ColorType.LIGHT_SQUARE :
                        ChessBoardColorScheme.ColorType.DARK_SQUARE;


                if (highlight.contains(pos)) {
                    type = (lightSquare) ? ChessBoardColorScheme.ColorType.HIGHLIGHT_MOVES_LIGHT :
                                    ChessBoardColorScheme.ColorType.HIGHLIGHT_MOVES_DARK;
                }
                else if (differences.contains(pos)) {
                    type = ChessBoardColorScheme.ColorType.MOVE_MADE;
                }

                System.out.print(colorScheme.getColorEscapeSequence(type));

                ChessPiece piece = game.getBoard().getPiece(pos);
                if (piece == null) {
                    System.out.print(EscapeSequences.EMPTY);
                }
                else {
                    System.out.print(switch (piece.getTeamColor()) {
                        case WHITE -> colorScheme.getColorEscapeSequence(ChessBoardColorScheme.ColorType.WHITE_PIECE);
                        case BLACK -> colorScheme.getColorEscapeSequence(ChessBoardColorScheme.ColorType.BLACK_PIECE);
                    });
                    switch (piece.getPieceType()) {
                        case KING -> System.out.print(EscapeSequences.BLACK_KING);
                        case QUEEN -> System.out.print(EscapeSequences.BLACK_QUEEN);
                        case BISHOP -> System.out.print(EscapeSequences.BLACK_BISHOP);
                        case KNIGHT -> System.out.print(EscapeSequences.BLACK_KNIGHT);
                        case ROOK -> System.out.print(EscapeSequences.BLACK_ROOK);
                        case PAWN -> System.out.print(EscapeSequences.BLACK_PAWN);
                    }
                }
            }

            System.out.print(colorScheme.getColorEscapeSequence(ChessBoardColorScheme.ColorType.BORDER_TEXT));
            System.out.print(colorScheme.getColorEscapeSequence(ChessBoardColorScheme.ColorType.BORDER));
            System.out.print(" " + row + ' ');
            System.out.print(EscapeSequences.RESET_BG_COLOR);
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            System.out.println();
        }


        printHeader(colorScheme, perspective);
        System.out.println();
    }


    private static void printHeader(ChessBoardColorScheme colorScheme, ChessGame.TeamColor perspective) {
        System.out.print(colorScheme.getColorEscapeSequence(ChessBoardColorScheme.ColorType.BORDER_TEXT));
        System.out.print(colorScheme.getColorEscapeSequence(ChessBoardColorScheme.ColorType.BORDER));
        System.out.print("   ");

        if (perspective == ChessGame.TeamColor.BLACK) {
            for (char c = 'h'; c >= 'a'; c--) {
                System.out.print("\u2003" + c + ' ');
            }
        } else {
            for (char c = 'a'; c <= 'h'; c++) {
                System.out.print("\u2003" + c + ' ');
            }
        }

        System.out.print("   ");

        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.println();
    }

    private static Collection<ChessPosition> determineDifferences(ChessGame newGame, ChessGame baseGame) {
        Collection<ChessPosition> differences = new HashSet<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = baseGame.getBoard().getPiece(pos);
                ChessPiece prevPiece = newGame.getBoard().getPiece(pos);

                if (!Objects.equals(piece, prevPiece)) {
                    differences.add(pos);
                }
            }
        }
        return differences;
    }

}
