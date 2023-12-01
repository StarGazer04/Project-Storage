//Import Section
import java.util.Random;
import java.util.Scanner;

/*
 * Provided in this class is the neccessary code to get started with your game's implementation
 * You will find a while loop that should take your minefield's gameOver() method as its conditional
 * Then you will prompt the user with input and manipulate the data as before in project 2
 * 
 * Things to Note:
 * 1. Think back to project 1 when we asked our user to give a shape. In this project we will be asking the user to provide a mode. Then create a minefield accordingly
 * 2. You must implement a way to check if we are playing in debug mode or not.
 * 3. When working inside your while loop think about what happens each turn. We get input, user our methods, check their return values. repeat.
 * 4. Once while loop is complete figure out how to determine if the user won or lost. Print appropriate statement.
 */
public class main {
    public static void main(String[] args) {

        int row;
        int col;
        int mines; // represents number of mines
        int flags; // number of flags
        int x;
        int y;
        boolean flag; // if user chooses a flag
        String f; // string input of the users choice of a flag or not

        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to Minesweeper! Please choose difficulty: Easy, Medium, Hard");
        String difficulty = input.nextLine();
        while(!difficulty.equals("Easy") && !difficulty.equals("Medium") && !difficulty.equals("Hard")){
            System.out.print("Invalid input, Please choose difficulty: Easy, Medium, Hard ");
            difficulty = input.nextLine();
        }
        if(difficulty.equals("Easy")){
            row = 5;
            col = 5;
            mines = 5;
            flags = 5;
        }
        else if(difficulty.equals("Medium")){
            row = 9;
            col = 9;
            mines = 12;
            flags = 12;
        }
        else{
            row = 20;
            col = 20;
            mines = 40;
            flags = 40;
        }
        Minefield minefield = new Minefield(row, col, flags);
        System.out.print("Enter starting coordinates: [x] [y]");
        String start = input.nextLine();
        String[] startParts = start.split("\\s+"); // google reference to splitting white spaces for user input
        while(true){
            while(startParts.length != 2){
                System.out.print("Either not 2 didgets or no spaces between, please try again");
                start = input.nextLine();
                startParts = start.split("\\s+");
            }
            try{
                x = Integer.parseInt(startParts[0]);
                y = Integer.parseInt(startParts[1]);
                while(x < 0 || y < 0 || x > row - 1 || y > col - 1){
                    System.out.print("Either not 2 didgets or no spaces between, please try again");
                    start = input.nextLine();
                    startParts = start.split("\\s+");
                    x = Integer.parseInt(startParts[0]);
                    y = Integer.parseInt(startParts[1]);
                }
                break;
            }
            catch (NumberFormatException e){
                System.out.print("A NumberExceptionError has occured, please try again");
            }
            System.out.print("Enter starting coordinates: [x] [y]");
            start = input.nextLine();
            startParts = start.split("\\s+");
        }
        minefield.createMines(x,y,mines);
        minefield.evaluateField();
        System.out.println("Please choose if you want debug mode: debug mode[d], no debug mode [nd]");
        String debug = input.nextLine();
        while(!debug.equals("d") && !debug.equals("nd")){
            System.out.println("Invalid input, Please if you want to play on debug mode: ");
            debug = input.nextLine();
        }
        if(debug.equals("d")){
            System.out.println("debug version:");
            minefield.debug();
        }
        //minefield.debug();
        System.out.print("\n");
        System.out.println("=".repeat(col * 2)); // found repeat call of google search. used it to make terminal output more professional looking
        minefield.revealStartingArea(x, y);
        System.out.print(minefield);
        System.out.print("\n");
        System.out.println("=".repeat(col * 2));

         //begin game loop
        while(!minefield.gameOver()) {
            flag = false; //initialize to false
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter coordinates and if you wish to place a flag (remmaining: " + minefield.getNumFlags() +  "): [x] [y] [f(optional)]");
            String conditions = scanner.nextLine();
            String[] conditionParts = conditions.split("\\s+");
            while (true) {
                while (conditionParts.length != 3 && conditionParts.length != 2) {
                    System.out.print("Either not 3 inputs, or no spaces between, please try again");
                    conditions = scanner.nextLine();
                    conditionParts = conditions.split("\\s+");
                }
                try {
                    x = Integer.parseInt(conditionParts[0]);
                    y = Integer.parseInt(conditionParts[1]);
                    if (conditionParts.length == 3) {
                        f = conditionParts[2];
                    } else {
                        f = null;
                    }


                    while (x < 0 || y < 0 || x > row - 1 || y > col - 1 || (conditionParts.length == 3 && !f.equals("f"))) {
                        System.out.print("wrong input, please try again");
                        conditions = scanner.nextLine();
                        conditionParts = conditions.split("\\s+");
                        x = Integer.parseInt(conditionParts[0]);
                        y = Integer.parseInt(conditionParts[1]);
                        if (conditionParts.length == 3) {
                            f = conditionParts[2];
                        } else {
                            f = null;
                        }
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.print("A NumberExceptionError has occured, please try again");
                }
                System.out.print("Enter starting coordinates: [x] [y] [f(optional)]");
                conditions = scanner.nextLine();
                conditionParts = conditions.split("\\s+");
            }

            if (conditionParts.length == 3) {
                if (f.equals("f")) {
                    flag = true;
                }
            }
            //write if game is over statement below maybe
            if (minefield.guess(x, y, flag)) {
                ; // make this an if statement later
                System.out.print(minefield);
                System.out.print("\n");
                System.out.println("=".repeat(col * 2));
            }
        }
        System.out.print(minefield);
    }
}


