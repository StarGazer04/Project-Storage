// Tom Gordon 11/03/22

import org.junit.*;

import java.lang.StringBuilder;
import org.junit.runners.MethodSorters;

import java.io.PrintWriter;


/// VERY IMPORTANT ///



// All copies of the tests must have the NAME_ASCENDING
// option enabled. Otherwise there is no way to ensure
// that the bools.txt file will be synced with the tests.
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAllMaker {

    /**
     * These functions test every possible input for each of the verification
     * functions.
     *
     * i -> start row
     * j -> start col
     * k -> end row
     * l -> end col
     */

    @Test
    @WorthPoints(points = 6)
    @Category(category = "Source and Destination")
    public void sourceAndDestination() {
        bools.append("sou:\n");
        for(int i = -1; i < 9; i++){
            for(int j = -1; j < 9; j++){
                for(int k = -1; k < 9; k++) {
                    for (int l = -1; l < 9; l++) {
                        bools.append(board.verifySourceAndDestination(i, j, k, l, true) + " ");
                        bools.append(board.verifySourceAndDestination(i, j, k, l, false) + " ");
                    }
                    bools.append("\n");
                }
            }
        }
    }

    @Test
    @WorthPoints(points = 3)
    @Category(category = "Horizontal")
    public void horizontal(){
        bools.append("hor:\n");
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    for (int l = 0; l < 8; l++) {
                        bools.append(board.verifyHorizontal(i,j,k,l) + " ");
                    }
                    bools.append("\n");
                }
            }
        }
    }

    @Test
    @WorthPoints(points = 3)
    @Category(category = "Vertical")
    public void veritcal(){
        bools.append("ver:\n");
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    for (int l = 0; l < 8; l++) {
                        bools.append(board.verifyVertical(i,j,k,l) + " ");
                    }
                    bools.append("\n");
                }
            }
        }
    }

    @Test
    @WorthPoints(points = 3)
    @Category(category = "Diagonal")
    public void diagonal(){
        bools.append("dia:\n");
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    for (int l = 0; l < 8; l++) {
                        bools.append(board.verifyDiagonal(i,j,k,l) + " ");
                    }
                    bools.append("\n");
                }
            }
        }
    }

    @Test
    @WorthPoints(points = 3)
    @Category(category = "Adjacent")
    public void adjacentHorizontal(){
        bools.append("adj:\n");
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    for (int l = 0; l < 8; l++) {
                        bools.append(board.verifyAdjacent(i, j, k, l) + " ");
                    }
                    bools.append("\n");
                }
            }
        }
    }

    private static final ScoringRule SCORING_RULE = new ScoringRule(TestAllMoves.class);

    @Rule
    public ScoringRule scoringRule = SCORING_RULE;

    private static Board board;
    private final int a = 0;    // Adjust to 0 if student uses 1-8 over 0-7

    // The fol0ing will contain files with expected results for inalid verifications
    // This is my solution to not wanting a copy of each valid verification func here
    // All are static bc @AfterClass must tag a static method.
    private static StringBuilder bools;

    @BeforeClass
    public static void setUp() {
        board = new Board();
        board.clear();
        Fen.load("8/1p2P3/8/3p2P1/1P6/5p2/3P4/8", board);

        bools = new StringBuilder();
    }

    @AfterClass
    public static void flushFile() {
        try {
            PrintWriter out = new PrintWriter("bools.txt");
            out.print(bools);
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
