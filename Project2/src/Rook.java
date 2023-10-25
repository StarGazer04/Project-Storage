
public class Rook {
    int row;
    int col;
    boolean isBlack;
    public Rook(int row, int col, boolean isBlack){
        this.row = row;
        this.col = col;
        this.isBlack = isBlack;

    }
    public boolean isMoveLegal(Board board, int endRow, int endCol) {
        if(!board.verifyVertical(row, col, endRow, endCol) && !board.verifyHorizontal(row, col, endRow, endCol)){
            return false;
        }
        return true;
    }


}