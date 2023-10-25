
public class Knight {
    int row;
    int col;
    boolean isBlack;
    public Knight(int row, int col, boolean isBlack){
        this.row = row;
        this.col = col;
        this.isBlack = isBlack;

    }
    public boolean isMoveLegal(Board board, int endRow, int endCol) {
        if(Math.abs(endRow - row) == 2 && Math.abs(endCol - col) == 1){
                return true;
        }
        else if (Math.abs(endRow - row) == 1 && Math.abs(endCol - col) == 2){
            return true;
    }
        else{
            return false;
        }
    }
}