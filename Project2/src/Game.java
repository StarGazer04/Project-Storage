// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.

import java.util.Optional;
import java.util.Scanner;
public class Game {
    public static void main(String[] args) {
        //create board object and print it
        boolean val = true; //starting piece is black color
        System.out.println("Welcome to chess");
        Board myBoard = new Board();
        Fen.load("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", myBoard);
        System.out.println(myBoard.toString());
        //create scanner for user input to move chess piece
        Scanner input = new Scanner(System.in);
        boolean gameOver = myBoard.isGameOver();
        while (!myBoard.isGameOver()) {
            System.out.println("Choose set of integers, format [start row][start col][end row][end col]:");
            String stringCord = input.nextLine(); // starts off as a string to make sure user inputs 4 digits
            int cord;
            //try and catch to funnel out any letter output by user after verifying
            while (true) {
                //check that input is 4 long
                while (stringCord.length() != 4) {
                    System.out.println("Not four digits, please try again");
                    stringCord = input.nextLine();
                }
                try {
                    cord = Integer.parseInt(stringCord);
                    //check that input is within bounds
                    while(cord < 0 || cord > 7777){
                        System.out.println("not within bounds, please try again");
                        stringCord = input.nextLine();
                        cord = Integer.parseInt(stringCord);
                    }
                    break;
                } catch (NumberFormatException n) {
                    System.out.println("a NumberExceptionError has occured, please try again");

                }
                // new input
                System.out.println("wrong input, has to be in format [start row][start col][end row][end col]:");
                stringCord = input.nextLine();
            }
            // commands the user to type the correct shapes given through the list
            int endCol = cord % 10;
            int endRow = (cord / 10) % 10;
            int startCol = (cord / 100) % 10;
            int startRow = (cord / 1000) % 10;
            while (!myBoard.movePiece(startRow, startCol, endRow, endCol)) {
                System.out.println("unable to move piece, please type in valid cordinates");
                stringCord = input.nextLine();
                cord = Integer.parseInt(stringCord);
                endCol = cord % 10;
                endRow = (cord / 10) % 10;
                startCol = (cord / 100) % 10;
                startRow = (cord / 1000) % 10;
            }
            System.out.println(myBoard.toString());
            if (val) {
                val = false;
                System.out.println("White piece turn");
            } else {
                val = true;
                System.out.println("Black piece turn");
            }
        }
            //check for color (aka boolen value) and switch followed by print statement
        if (val) {
            System.out.print("White has won the game");
        }
        else{
            System.out.print("Black has won the game");
        }
        myBoard.gameVal = false;
        myBoard.clear();
    }
}