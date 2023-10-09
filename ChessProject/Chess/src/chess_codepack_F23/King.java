package chess_codepack_F23;
public class King {
    int row;
    int col;
    boolean isBlack;
    public King(int row, int col, boolean isBlack){
        this.row = row;
        this.col = col;
        this.isBlack = isBlack;

    }
    public boolean isMoveLegal(Board board, int endRow, int endCol) {
        if(!board.verifyHorizontal(row, col, endRow, endCol) && !board.verifyVertical(row, col, endRow, endCol)){
            return false;
        }
        return true;
    }


}