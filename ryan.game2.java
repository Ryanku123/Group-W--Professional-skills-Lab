import java.util.*;

// Game2_DiceGrid.java — Dice Grid Puzzle


public class Game2_DiceGrid implements Game {

    private static final String NAME = "Dice Grid Puzzle";
    private Random rand = new Random();
    private Scanner sc = new Scanner(System.in);

    @Override
    public String getName() { return NAME; }

    @Override
    public int play(User user) {
        System.out.println("\n==============================");
        System.out.println("  GAME 2: Dice Grid Puzzle");
        System.out.println("==============================");
        System.out.println("Roll a die, then place it in an empty cell (row col, e.g. '2 3').");
        System.out.println("Score is based on rows and columns after all 9 cells are filled.\n");

        int[][] grid = new int[3][3];  // 0 means the cell is empty
        int remaining = 9;

        while (remaining > 0) {
            int rolled = rand.nextInt(6) + 1;
            System.out.println("You rolled: " + rolled);
            printGrid(grid);

            // Keep asking until the player picks a valid empty cell
            int row = -1, col = -1;
            while (true) {
                System.out.print("Place it (row col, 1-based): ");
                try {
                    String[] parts = sc.nextLine().trim().split("\\s+");
                    row = Integer.parseInt(parts[0]) - 1;  // Convert to 0-based index
                    col = Integer.parseInt(parts[1]) - 1;
                    if (row < 0 || row > 2 || col < 0 || col > 2) {
                        System.out.println("Out of range. Use 1-3.");
                    } else if (grid[row][col] != 0) {
                        System.out.println("Cell already taken. Choose another.");
                    } else {
                        break;  // Valid cell chosen
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input. Try again.");
                }
            }
            grid[row][col] = rolled;
            remaining--;
        }

        System.out.println("\nFinal Grid:");
        printGrid(grid);
        int totalScore = scoreGrid(grid);
        System.out.println("Total Score: " + totalScore + " pts");
        return totalScore;
    }

    /** Draws the current state of the 3x3 grid with borders and labels. */
    private void printGrid(int[][] grid) {
        System.out.println("  +---+---+---+");
        for (int r = 0; r < 3; r++) {
            System.out.print((r + 1) + " |");
            for (int c = 0; c < 3; c++) {
                // Show "." for empty cells, the value otherwise
                System.out.print(" " + (grid[r][c] == 0 ? "." : grid[r][c]) + " |");
            }
            System.out.println();
        }
        System.out.println("  +---+---+---+");
        System.out.println("    1   2   3");
    }

    //
     // Scores all 3 rows and all 3 columns.
     // For columns, the values are extracted vertically from the 2D array.
     // Prints a per-line breakdown and returns the total.
     
    private int scoreGrid(int[][] grid) {
        int total = 0;
        System.out.println("\n--- Row Scores ---");
        for (int r = 0; r < 3; r++) {
            int[] line = {grid[r][0], grid[r][1], grid[r][2]};
            int s = scoreLine(line);
            System.out.println("Row " + (r + 1) + " " + Arrays.toString(line) + ": " + s + " pts (" + lineName(line) + ")");
            total += s;
        }
        System.out.println("\n--- Column Scores ---");
        for (int c = 0; c < 3; c++) {
            // Extract column values from 2D array
            int[] line = {grid[0][c], grid[1][c], grid[2][c]};
            int s = scoreLine(line);
            System.out.println("Col " + (c + 1) + " " + Arrays.toString(line) + ": " + s + " pts (" + lineName(line) + ")");
            total += s;
        }
        return total;
    }

    // Returns the score for a single 3-value line based on its pattern. */
    private int scoreLine(int[] line) {
        int[] sorted = line.clone(); Arrays.sort(sorted);
        Map<Integer, Integer> freq = new HashMap<>();
        for (int d : sorted) freq.merge(d, 1, Integer::sum);
        List<Integer> counts = new ArrayList<>(freq.values());
        Collections.sort(counts, Collections.reverseOrder());
        if (counts.get(0) == 3) return 15;    // Three of a Kind
        if (isStraight3(sorted))  return 12;  // Straight
        if (counts.get(0) == 2)   return 8;   // Pair
        return 5;                              // All Different
    }

    // Returns the pattern name for a single 3-value line (used for display). */
    private String lineName(int[] line) {
        int[] sorted = line.clone(); Arrays.sort(sorted);
        Map<Integer, Integer> freq = new HashMap<>();
        for (int d : sorted) freq.merge(d, 1, Integer::sum);
        List<Integer> counts = new ArrayList<>(freq.values());
        Collections.sort(counts, Collections.reverseOrder());
        if (counts.get(0) == 3) return "Three of a Kind";
        if (isStraight3(sorted)) return "Straight";
        if (counts.get(0) == 2) return "Pair";
        return "All Different";
    }

    //
     // Checks whether 3 sorted values form a consecutive straight
     //(e.g. [2,3,4] or [4,5,6]).
     
    private boolean isStraight3(int[] sorted) {
        return sorted[1] == sorted[0] + 1 && sorted[2] == sorted[1] + 1;
    }
}
