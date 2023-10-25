
public class Board {
    // Instance variables
    private Piece[][] board;
    private boolean isBlackTurn = true;
    public boolean gameVal = true; // will change if game is ended

    //TODO:
    // Construct an object of type Board using given arguments.
    public Board() {
        board = new Piece[8][8];
    }

    // Accessor Methods

    //TODO:
    // Return the Piece object stored at a given row and column
    public Piece getPiece(int row, int col) {
        Piece piece = board[row][col];
        return piece;//return null;
    }

    //TODO:
    // Update a single cell of the board to the new piece.
    public void setPiece(int row, int col, Piece piece) {
        board[row][col] = piece;
    }

    // Game functionality methods

    //TODO:
    // Moves a Piece object from one cell in the board to another, provided that
    // this movement is legal. Returns a boolean to signify success or failure.
    // This method calls all necessary helper functions to determine if a move
    // is legal, and to execute the move if it is. Your Game class should not 
    // directly call any other method of this class.
    // Hint: this method should call isMoveLegal() on the starting piece. 
    public boolean movePiece(int startRow, int startCol, int endRow, int endCol) {
        Piece startPiece = getPiece(startRow, startCol);
        if (!verifySourceAndDestination(startRow, startCol, endRow, endCol, isBlackTurn) ||  !startPiece.isMoveLegal(this, endRow, endCol)) {
            return false;
        }
        startPiece.setPosition(endRow, endCol);
        setPiece(endRow, endCol, startPiece);
        setPiece(startRow, startCol, null);
        if (startPiece.character == '\u265f' || startPiece.character == '\u2659') {
            startPiece.promotePawn(endRow, startPiece.isBlack);
        }
        if (!isGameOver()) {
            if (isBlackTurn) {
                isBlackTurn = false;
            }
            else {
                isBlackTurn = true;
            }
        }
        else{
            gameVal = false; // ends while loop in Game.class
        }
        return true;
    }

    //TODO:
    // Determines whether the game has been ended, i.e., if one player's King
    // has been captured.
    public boolean isGameOver() {
        int kingCount = 0; // initial king count
        // initialize nested for loop to iterate through 2d array to count number of kings
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece != null) {
                    if (piece.character == ('\u2654') || piece.character == ('\u265a')) {
                        kingCount += 1;
                    }
                }
            }
        }
        if (kingCount == 1) {
            // king has been captured
            return true;
        }
        else {
            return false;
        }
    }

    // Constructs a String that represents the Board object's 2D array.
    // Returns the fully constructed String.
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(" ");
        for (int i = 0; i < 8; i++) {
            out.append(" ");
            out.append(i);
        }
        out.append('\n');
        for (int i = 0; i < board.length; i++) {
            out.append(i);
            out.append("|");
            for (int j = 0; j < board[0].length; j++) {
                out.append(board[i][j] == null ? "\u2001|" : board[i][j] + "|");
            }
            out.append("\n");
        }
        return out.toString();
    }

    //TODO:
    // Sets every cell of the array to null. For debugging and grading purposes.
    public void clear() {
        for (int i = 0; i < 8; i ++){
            for (int j = 0; j < 8; j ++){
                board[i][j] = null;

            }
        }
    }

    // Movement helper functions

    //TODO:
    // Ensure that the player's chosen move is even remotely legal.
    // Returns a boolean to signify whether:
    // - 'start' and 'end' fall within the array's bounds.
    // - 'start' contains a Piece object, i.e., not null.
    // - Player's color and color of 'start' Piece match.
    // - 'end' contains either no Piece or a Piece of the opposite color.
    public boolean verifySourceAndDestination(int startRow, int startCol, int endRow, int endCol, boolean isBlack) {
        if (startRow >= 0 && startRow <= 7 && endRow <= 7 && endRow >= 0 && startCol >= 0 && startCol <= 7 && endCol <= 7 && endCol >= 0) {
            Piece startPiece = board[startRow][startCol];
            Piece endPiece = board[endRow][endCol];

            if (endPiece != null){
                if (startRow == endRow && endCol == startCol) {
                    System.out.println("Can't keep piece is same position");
                    return false;

                }
                if (endPiece.getIsBlack() == isBlack) {
                    System.out.println("Invalid, cannot capture your own piece");
                    return false;
                }
            }
            if (startPiece == null) {
                System.out.println("Invalid, cannot move empty space");
                return false;
            }
            else if (startPiece.getIsBlack() != isBlack) {
                System.out.println("Invalid starting piece does not match your color");
                return false;
            }
            else {
                return true;
            }
        }
        System.out.println("Invalid choice, out of bounds");
        return false;
    }

        //TODO:
        // Check whether the 'start' position and 'end' position are adjacent to each other
        public boolean verifyAdjacent ( int startRow, int startCol, int endRow, int endCol){
            int rowDif = Math.abs(endRow - startRow);
            int colDif = Math.abs(endCol - startCol);
            if (rowDif > 1 || colDif > 1) {
                System.out.println("invalid move, can only move one square at a time");
                return false;
            }
            System.out.println("Valid move");
            return true;
        }

    //TODO:
    // Checks whether a given 'start' and 'end' position are a valid horizontal move.
    // Returns a boolean to signify whether:
    // - The entire move takes place on one row.
    // - All spaces directly between 'start' and 'end' are empty, i.e., null.
    public boolean verifyHorizontal(int startRow, int startCol, int endRow, int endCol) {
        if (startRow != endRow) {
            return false;
        }
        else {
            for (int c = Math.min(startCol, endCol) + 1; c < Math.max(startCol, endCol); c++) {
                if (board[startRow][c] != null) {
                    System.out.println("Invalid move, there is a piece obstructing your move");
                    return false;
                }
            }
            return true;
        }
    }



    //TODO:
    // Checks whether a given 'start' and 'end' position are a valid vertical move.
    // Returns a boolean to signify whether:
    // - The entire move takes place on one column.
    // - All spaces directly between 'start' and 'end' are empty, i.e., null.
    public boolean verifyVertical(int startRow, int startCol, int endRow, int endCol) {
        if (startCol != endCol){
            return false;
        }
        else{
            for (int r = Math.min(startRow, endRow) + 1; r < Math.max(startRow, endRow); r ++){
                if (board[r][endCol] != null){
                    System.out.println("Invalid move, there is a piece obstructing your move");
                    return false;
                }
            }
            return true;
        }
    }

    //TODO:
    // Checks whether a given 'start' and 'end' position are a valid diagonal move.
    // Returns a boolean to signify whether:
    // - The path from 'start' to 'end' is diagonal... change in row and col.
    // - All spaces directly between 'start' and 'end' are empty, i.e., null.
    public boolean verifyDiagonal(int startRow, int startCol, int endRow, int endCol) {
        int rowIncrement = 0;
        int colIncrement = 0;
        if (endRow > startRow){
            rowIncrement = 1;
        }
        if(endCol > startCol){
            colIncrement = 1;
        }
        if (endRow < startRow){
            rowIncrement = -1;
        }
        if(endCol < startCol){
            colIncrement = -1;
        }
        if (Math.abs(endRow-startRow) != Math.abs(endCol - startCol)){
            System.out.print("invalid move, has to move diagnol");
            return false;
        }
        while(endRow - rowIncrement != startRow && endCol != startCol - colIncrement){
            if (board[startRow + rowIncrement][startCol + colIncrement] != null){
                System.out.print("Invalid move, object in the way");
                return false;
            }
            startCol += colIncrement;
            startRow += rowIncrement;
        }
        return true;
    }
}

