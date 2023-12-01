import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Minefield {
    /**
    Global Section
    */
    public static final String ANSI_YELLOW_BRIGHT = "\u001B[33;1m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED_BRIGHT = "\u001b[31;1m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001b[47m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001b[45m";
    public static final String ANSI_GREY_BACKGROUND = "\u001b[0m";
    public static final String GREEN_BRIGHT = "\033[0;92m";  // added myself
    public static final String PURPLE_BRIGHT = "\033[0;95m";
    public static final String CYAN_BRIGHT = "\033[0;96m";
    public static final String WHITE_BRIGHT = "\033[0;97m";
    public static final String RED_BOLD_BRIGHT = "\033[1;91m";


    /* 
     * Class Variable Section
     * 
    */
    Cell [][] mineField;
    Cell [][] extraMinefield; //used when user marks flag and removes flag on mineField.
    private int rows;
    private int columns;
    private int numFlags;
    private int mineCount;

    /*Things to Note:
     * Please review ALL files given before attempting to write these functions.
     * Understand the Cell.java class to know what object our array contains and what methods you can utilize
     * Understand the StackGen.java class to know what type of stack you will be working with and methods you can utilize
     * Understand the QGen.java class to know what type of queue you will be working with and methods you can utilize
     */
    
    /**
     * Minefield
     * 
     * Build a 2-d Cell array representing your minefield.
     * Constructor
     * @param rows       Number of rows.
     * @param columns    Number of columns.
     * @param flags      Number of flags, should be equal to mines
     */
    public Minefield(int rows, int columns, int flags) {
        mineField = new Cell[rows][columns];
        extraMinefield = new Cell [rows][columns];
        this.rows = rows;
        this.columns = columns;
        numFlags = flags;
    }

    /**
     * evaluateField
     * 
     *
     * @function:
     * Evaluate entire array.
     * When a mine is found check the surrounding adjacent tiles. If another mine is found during this check, increment adjacent cells status by 1.
     * 
     */
    public void evaluateField() {
        int [][] directions = {{0,1}, {1,0}, {0, -1}, {-1, 0}, {1,1}, {1,-1}, {-1, 1}, {-1, -1}};
        for(int i = 0; i < mineField.length; i ++){
            for (int j = 0; j < mineField[i].length; j ++){
                int numOfMines = 0;
                if(!mineField[i][j].getStatus().equals("M")) {
                    for (int[] direction : directions) {
                        int x = direction[0];
                        int y = direction[1];
                        if (!(j + x < 0 || j + x >= mineField[i].length || i + y < 0 || i + y >= mineField.length)) {
                            if (mineField[i + y][j + x].getStatus().equals("M")) {
                                numOfMines += 1;
                            }
                        }
                    }
                    String s = Integer.toString(numOfMines);
                    mineField[i][j].setStatus(s);
                }
            }
        }
        makeOrignialField();
    }

    /**
     * createMines
     * 
     * Randomly generate coordinates for possible mine locations.
     * If the coordinate has not already been generated and is not equal to the starting cell set the cell to be a mine.
     * utilize rand.nextInt()
     * 
     * @param x       Start x, avoid placing on this square.
     * @param y        Start y, avoid placing on this square.
     * @param mines      Number of mines to place.
     */
    public void createMines(int x, int y, int mines) {
        mineCount = 0; //number of mines placed
        // create a blank board with cells
        for (int i = 0; i < mineField.length; i ++){
            for( int j = 0; j < mineField[i].length; j ++){
                mineField[i][j] = new Cell(false, "-");
            }
        }
        Random rand = new Random();
        while (mineCount < mines){
            int xCord = rand.nextInt(rows);
            int yCord = rand.nextInt(columns);
            while (mineField[xCord][yCord].getStatus().equals("M") || (xCord == x && yCord == y)){ // potential remove to debug later
                xCord = rand.nextInt(rows);
                yCord = rand.nextInt(columns);
            }
            mineField[xCord][yCord].setStatus("M");
            mineCount += 1;
        }

    }

    /**
     * guess
     * 
     * Check if the guessed cell is inbounds (if not done in the Main class). 
     * Either place a flag on the designated cell if the flag boolean is true or clear it.
     * If the cell has a 0 call the revealZeroes() method or if the cell has a mine end the game.
     * At the end reveal the cell to the user.
     * 
     * 
     * @param x       The x value the user entered.
     * @param y       The y value the user entered.
     * @param flag    A boolean value that allows the user to place a flag on the corresponding square.
     * @return boolean Return false if guess did not hit mine or if flag was placed, true if mine found.
     */
    public boolean guess(int x, int y, boolean flag) {
        if(flag) {
            if (mineField[x][y].getStatus().equals("F")) {
                mineField[x][y].setStatus(extraMinefield[x][y].getStatus()); // used back up minefield to get the value before the flag was used by the user
                mineField[x][y].setRevealed(false);
                numFlags ++;
                return true;
            } else if (numFlags > 0 && !mineField[x][y].getRevealed()) {
                mineField[x][y].setStatus("F");
                mineField[x][y].setRevealed(true);
                numFlags--;
                return true;
            }
            else if(numFlags <= 0){
                System.out.println("Flag limit has been reached");
                return false;
            }
            else{
                System.out.println("Can't Place flag on an already revealed spot");
                return false;
            }
        }
        else {
            if (mineField[x][y].getRevealed()) {
                System.out.println("Can't reveal a cell that is already revealed");
                return false;
            }
            else if (mineField[x][y].getStatus().equals("0")) {
                revealZeroes(x, y);
                return true;
            }
            else if (mineField[x][y].getStatus().equals("M")) {
                System.out.println("You guessed a mine!");
                mineField[x][y].setRevealed(true);
                return false;
            }
            else {
                mineField[x][y].setRevealed(true);
                return true;
            }
        }
    }

    /**
     * gameOver
     * 
     * Ways a game of Minesweeper ends:
     * 1. player guesses a cell with a mine: game over -> player loses
     * 2. player has revealed the last cell without revealing any mines -> player wins
     * 
     * @return boolean Return false if game is not over and squares have yet to be revealed, otheriwse return true.
     */
    public boolean gameOver() {
        int numRevealed = 0;
        for (int i = 0; i < mineField.length; i++) {
            for (int j = 0; j < mineField[i].length; j++) {
                // if mine was found and was revealed
                if (mineField[i][j].getRevealed() && mineField[i][j].getStatus().equals("M")) {
                    System.out.println("Game Over");
                    return true;
                // checks to see if user won by seeing if all non mine cells are revealed
                } else {
                    if(numRevealed == (rows * columns) - mineCount -1){
                        System.out.println("You win");
                        return true;
                    }
                    else if (mineField[i][j].getRevealed() && !mineField[i][j].getStatus().equals("M") && !mineField[i][j].getStatus().equals("F")) {
                        numRevealed++;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Reveal the cells that contain zeroes that surround the inputted cell.
     * Continue revealing 0-cells in every direction until no more 0-cells are found in any direction.
     * Utilize a STACK to accomplish this.
     *
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a stack be useful here rather than a queue?
     *
     * @param x      The x value the user entered.
     * @param y      The y value the user entered.
     */
    public void revealZeroes(int x, int y) {
        Stack1Gen<int []> stack = new Stack1Gen<>();
        stack.push(new int[] {x, y});
        while(!stack.isEmpty()){
            int [] position = stack.pop();
            int xDirection = position[0];
            int yDirection = position[1];
            mineField[xDirection][yDirection].setRevealed(true);
            int [][] directions = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
            for(int [] direction: directions){
                int newX = xDirection + direction[0];
                int newY = yDirection + direction[1];
                if(newX >= 0  && newX < rows && newY >= 0 && newY < columns && !mineField[newX][newY].getRevealed() && mineField[newX][newY].getStatus().equals("0") ) {
                    if(direction[0] + direction[1] == 1 || direction[0] + direction[1] == -1) {
                        stack.push(new int[]{newX, newY});
                    }
                }
                else if(newX >= 0  && newX < rows && newY >= 0 && newY < columns && !mineField[newX][newY].getRevealed()){
                    mineField[newX][newY].setRevealed(true);
                }

            }
        }

    }

    /**
     * revealStartingArea
     *
     * On the starting move only reveal the neighboring cells of the inital cell and continue revealing the surrounding concealed cells until a mine is found.
     * Utilize a QUEUE to accomplish this.
     * 
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a queue be useful for this function?
     *
     * @param x     The x value the user entered.
     * @param y     The y value the user entered.
     */
    public void revealStartingArea(int x, int y) {
        boolean mineEncountered = false;
        Q1Gen<int[]> startArea = new Q1Gen<>();
        int [][] directions = {{0,1}, {0, -1}, {-1, 0}, {1, 0}};
        startArea.add(new int[] {x, y});
        while(startArea.length() > 0 && !mineEncountered) {
            int [] cords = startArea.remove();
            int xDirection = cords[0];
            int yDirection = cords[1];
            if (mineField[xDirection][yDirection].getStatus().equals("M")) {
                mineEncountered = true;
            }
            if(!mineField[xDirection][yDirection].getStatus().equals("M")) {
                mineField[xDirection][yDirection].setRevealed(true);
                if(mineField[xDirection][yDirection].getStatus().equals("0")){
                    revealZeroes(xDirection, yDirection);
                }

                for (int[] direction : directions) {
                    int newX = xDirection + direction[0];
                    int newY = yDirection + direction[1];
                    if (newX >= 0 && newX < rows && newY >= 0 && newY < columns && !mineField[newX][newY].getRevealed()) {
                        startArea.add(new int[]{newX, newY});
                    }
                }
            }
        }
    }

    /**
     * For both printing methods utilize the ANSI colour codes provided! 
     * 
     * 
     * 
     * 
     * 
     * debug
     *
     * @function This method should print the entire minefield, regardless if the user has guessed a square.
     * *This method should print out when debug mode has been selected. 
     */
    public void debug() {
        System.out.print("  "); //for organization purpose (makes board more clear to user)
        for(int r = 0; r < rows; r ++){
            System.out.print(" " + r);
        }
        for (int i = 0; i < mineField.length; i ++){
            System.out.print("\n");
            if(i <= 9){
                System.out.print(i + "  ");
            }
            else{
                System.out.print(i + " ");
            }
            for( int j = 0; j < mineField[i].length; j ++){
                if(j >= 10){
                    System.out.print(" ");
                }
                if(mineField[i][j].getStatus().equals("0")){
                    System.out.print(ANSI_RED_BRIGHT+"0 "+ANSI_GREY_BACKGROUND);
                }
                else if(mineField[i][j].getStatus().equals("1")){
                    System.out.print(ANSI_BLUE_BRIGHT+"1 "+ANSI_GREY_BACKGROUND);
                }
                else if(mineField[i][j].getStatus().equals("2")){
                    System.out.print(ANSI_PURPLE+"2 "+ANSI_GREY_BACKGROUND);
                }
                else if(mineField[i][j].getStatus().equals("3")){
                    System.out.print(ANSI_YELLOW+"3 "+ANSI_GREY_BACKGROUND);
                }
                else if(mineField[i][j].getStatus().equals("4")){
                    System.out.print(ANSI_BLUE+"4 "+ANSI_GREY_BACKGROUND);
                }
                else if(mineField[i][j].getStatus().equals("5")){
                    System.out.print(ANSI_CYAN+"5 "+ANSI_GREY_BACKGROUND);
                }
                else if(mineField[i][j].getStatus().equals("6")){
                    System.out.print(ANSI_GREEN+"6 "+ANSI_GREY_BACKGROUND);
                }
                else if(mineField[i][j].getStatus().equals("7")){
                    System.out.print(CYAN_BRIGHT+"7 "+ANSI_GREY_BACKGROUND);
                }
                else if(mineField[i][j].getStatus().equals("8")){
                    System.out.print(ANSI_YELLOW_BRIGHT+"8 "+ANSI_GREY_BACKGROUND);
                }
                else if(mineField[i][j].getStatus().equals("9")){
                    System.out.print(GREEN_BRIGHT+"9 "+ANSI_GREY_BACKGROUND);
                }
                else if(mineField[i][j].getStatus().equals("-")){
                    System.out.print(WHITE_BRIGHT+"- "+ANSI_GREY_BACKGROUND);
                }
                else{
                    System.out.print(RED_BOLD_BRIGHT+"M "+ANSI_GREY_BACKGROUND);
                }




            }
        }

    }

    /**
     * toString
     *
     * @return String The string that is returned only has the squares that has been revealed to the user or that the user has guessed.
     */
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append(" "); //for organization purpose (makes board more clear to user)
        //for column indexes
        for(int r = 0; r < rows; r ++){
            string.append(" " + r);
        }
        for(int i = 0; i < mineField.length; i  ++){
            string.append("\n");
            string.append(i + " "); // row row indexes
            for(int j = 0; j < mineField[i].length; j++){
                if(mineField[i][j].getRevealed()){
                    if(mineField[i][j].getStatus().equals("0")){
                        string.append(ANSI_RED_BRIGHT+mineField[i][j].getStatus()+ANSI_GREY_BACKGROUND + " ");
                    }
                    else if(mineField[i][j].getStatus().equals("1")){
                        string.append(ANSI_BLUE_BRIGHT+mineField[i][j].getStatus()+ANSI_GREY_BACKGROUND + " ");
                    }
                    else if(mineField[i][j].getStatus().equals("2")){
                        string.append(ANSI_PURPLE+mineField[i][j].getStatus()+ANSI_GREY_BACKGROUND + " ");
                    }
                    else if(mineField[i][j].getStatus().equals("3")){
                        string.append(ANSI_YELLOW+mineField[i][j].getStatus()+ANSI_GREY_BACKGROUND + " ");
                    }
                    else if(mineField[i][j].getStatus().equals("4")){
                        string.append(ANSI_BLUE+mineField[i][j].getStatus()+ANSI_GREY_BACKGROUND + " ");
                    }
                    else if(mineField[i][j].getStatus().equals("5")){
                        string.append(ANSI_CYAN+mineField[i][j].getStatus()+ANSI_GREY_BACKGROUND + " ");
                    }
                    else if(mineField[i][j].getStatus().equals("6")){
                        string.append(ANSI_GREEN+mineField[i][j].getStatus()+ANSI_GREY_BACKGROUND + " ");
                    }
                    else if(mineField[i][j].getStatus().equals("7")){
                        string.append(CYAN_BRIGHT+mineField[i][j].getStatus()+ANSI_GREY_BACKGROUND + " ");
                    }
                    else if(mineField[i][j].getStatus().equals("8")){
                        string.append(ANSI_YELLOW_BRIGHT+mineField[i][j].getStatus()+ANSI_GREY_BACKGROUND + " ");
                    }
                    else if(mineField[i][j].getStatus().equals("9")){
                        string.append(GREEN_BRIGHT+mineField[i][j].getStatus()+ANSI_GREY_BACKGROUND + " ");
                    }
                    else if(mineField[i][j].getStatus().equals("-")){
                        string.append(WHITE_BRIGHT+mineField[i][j].getStatus()+ANSI_GREY_BACKGROUND + " ");
                    }
                    else{
                        string.append(RED_BOLD_BRIGHT+mineField[i][j].getStatus()+ANSI_GREY_BACKGROUND + " ");
                    }

                }
                else{
                    string.append(WHITE_BRIGHT+"- "+ANSI_GREY_BACKGROUND);
                }
            }
        }
        return string.toString();
    }

    // helper method for flag
    public void makeOrignialField(){
        for (int i = 0; i < mineField.length; i ++){
            for(int j = 0; j < mineField[i].length; j ++){
                extraMinefield [i][j] = new Cell(mineField[i][j].getRevealed(), mineField[i][j].getStatus());
            }
        }
    }

    // getter method for number of flags
    public int getNumFlags(){
        return numFlags;
    }
}
