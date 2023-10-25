

import java.util.Scanner;
import java.util.ArrayList; //will be used to create an array of string representation of pieces later for pawn promotion
public class Piece {
    // Create Instance Variables
    char character;
    int row;
    int col;
    boolean isBlack;

    /**
     * Constructor.
     * @param character     The character representing the piece.
     * @param row           The row on the board the piece occupies.
     * @param col           The column on the board the piece occupies.
     * @param isBlack       The color of the piece.
     */
    public Piece(char character, int row, int col, boolean isBlack) {
        this.character = character;
        this.row = row;
        this.col = col;
        this.isBlack = isBlack;
    }

    /**
     * Determines if moving this piece is legal.
     * @param board     The current state of the board.
     * @param endRow    The destination row of the move.
     * @param endCol    The destination column of the move.
     * @return If the piece can legally move to the provided destination on the board.
     */
    public boolean isMoveLegal(Board board, int endRow, int endCol) {
        switch (this.character) {
            case '\u2659':
            case '\u265f':
                Pawn pawn = new Pawn(row, col, isBlack);
                return pawn.isMoveLegal(board, endRow, endCol);
            case '\u2656':
            case '\u265c':
                Rook rook = new Rook(row, col, isBlack);
                return rook.isMoveLegal(board, endRow, endCol);
            case '\u265e':
            case '\u2658':
                Knight knight = new Knight(row, col, isBlack);
                return knight.isMoveLegal(board, endRow, endCol);
            case '\u265d':
            case '\u2657':
                Bishop bishop = new Bishop(row, col, isBlack);
                return bishop.isMoveLegal(board, endRow, endCol);
            case '\u265b':
            case '\u2655':
                Queen queen = new Queen(row, col, isBlack);
                return queen.isMoveLegal(board, endRow, endCol);
            case '\u265a':
            case '\u2654':
                King king = new King(row, col, isBlack);
                return king.isMoveLegal(board, endRow, endCol);
            default:
                return false;
        }
    }
    /**
     * Sets the position of the piece.
     * @param row   The row to move the piece to.
     * @param col   The column to move the piece to.
     */
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Return the color of the piece.
     * @return  The color of the piece.
     */
    public boolean getIsBlack() {
        return (isBlack);
    }

    /**
     * Handle promotion of a pawn.
     * @param row Current row of the pawn
     * @param isBlack Color of the pawn
     */
    public void promotePawn(int row, boolean isBlack) {
        Scanner input = new Scanner(System.in);
        ArrayList<String> pieceList = new ArrayList<>();
        pieceList.add("Queen");
        pieceList.add("Rook");
        pieceList.add("Knight");
        pieceList.add("Bishop");

        if (isBlack) {
            if (row == 7) {
                System.out.print("Choose which piece you would like to change your pawn to. options are: Queen, Rook, Bishop, Knight ");
                String changePiece = input.nextLine();
                //check that user input is the correct string
                while (!pieceList.contains(changePiece)) {
                    System.out.print("Not an option, please try again:");
                    changePiece = input.next();
                }
                //change piece for black color
                if (changePiece.equals("Queen")) {
                    character = '\u265b';
                } else if (changePiece.equals("Knight")) {
                    character = '\u265e';
                } else if (changePiece.equals("Bishop")) {
                    character = '\u265d';
                } else {
                    character = '\u265c';
                }
            }
        }
        else {
            if (row == 0) {
                System.out.print("Choose which piece you would like to change your pawn to. options are: Queen, Rook, Bishop, Knight ");
                String changePiece = input.nextLine();
                //check that user input is the correct string
                while (!pieceList.contains(changePiece)) {
                    System.out.print("Not an option, please try again:");
                    changePiece = input.next();
                }
                //change piece for black color
                if (changePiece.equals("Queen")) {
                    character = '\u2655';
                } else if (changePiece.equals("Knight")) {
                    character = '\u2654';
                } else if (changePiece.equals("Bishop")) {
                    character = '\u2657';
                } else {
                    character = '\u2656';
                }
            }
        }
    }

        /**
         * Returns a string representation of the piece.
         * @return A string representation of the piece.
         */
        public String toString () {
            StringBuilder in = new StringBuilder();
            in.append(character);
            return (in.toString());
        }

    }

