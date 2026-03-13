import java.util.*;

public class Game2_DiceGrid implements Game {

    private static final String NAME = "Dice Grid Puzzle";
    private Random rand = new Random();
    private Scanner sc = new Scanner(System.in);

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int play(User user) {
        int[][] grid = new int[3][3]; 
        int turns = 0;

        System.out.println("\n==============================");
        System.out.println("  GAME 2: " + NAME);
        System.out.println("==============================");
        System.out.println("Roll a die, then place it in an empty cell (row col, e.g. '2 3').");
        System.out.println("Score is based on rows and columns after all 9 cells are filled.\n");

        while (turns < 9) {
            int rolled = rand.nextInt(6) + 1;
            System.out.println("You rolled: " + rolled);
            printGrid(grid);

            int row = -1, col = -1;
            boolean validPlacement = false;

            while (!validPlacement) {
                System.out.print("Place it (row col, 1-based): ");
                try {
                    String input = sc.nextLine().trim();
                    String[] parts = input.split("\\s+");
                    
                    if (parts.length < 2) {
                        System.out.println("Invalid input. Please enter two numbers (row and column).");
                        continue;
                    }

                    row = Integer.parseInt(parts[0]) - 1; 
                    col = Integer.parseInt(parts[1]) - 1;

                    if (row < 0 || row > 2 || col < 0 || col > 2) {
                        System.out.println("Out of range. Use 1-3.");
                    } else if (grid[row][col] != 0) {
                        System.out.println("Cell already taken. Choose another.");
                    } else {
                        validPlacement = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Try again.");
                }
            }

            grid[row][col] = rolled;
            turns++;
        }

        System.out.println("\nFinal Grid:");
        printGrid(grid);

        int totalScore = scoreGrid(grid);
        System.out.println("\nTotal Score: " + totalScore + " pts");
        
        return totalScore;
    }

    private void printGrid(int[][] grid) {
        System.out.println("    +---+---+---+");
        for (int r = 0; r < 3; r++) {
            System.out.print("  " + (r + 1) + " | ");
            for (int c = 0; c < 3; c++) {
                String val = (grid[r][c] == 0) ? "." : String.valueOf(grid[r][c]);
                System.out.print(val + " | ");
            }
            System.out.println("\n    +---+---+---+");
        }
        System.out.println("      1   2   3");
    }

    private int scoreGrid(int[][] grid) {
        int total = 0;

        System.out.println("\n--- Row Scores ---");
        for (int r = 0; r < 3; r++) {
            int[] line = { grid[r][0], grid[r][1], grid[r][2] };
            int score = scoreLine(line);
            System.out.println("Row " + (r + 1) + " " + Arrays.toString(line) + ": " + score + " pts (" + lineName(line) + ")");
            total += score;
        }

        System.out.println("\n--- Column Scores ---");
        for (int c = 0; c < 3; c++) {
            int[] line = { grid[0][c], grid[1][c], grid[2][c] };
            int score = scoreLine(line);
            System.out.println("Col " + (c + 1) + " " + Arrays.toString(line) + ": " + score + " pts (" + lineName(line) + ")");
            total += score;
        }

        return total;
    }

    private int scoreLine(int[] line) {
        int[] sorted = line.clone();
        Arrays.sort(sorted);

        // Map for frequency check
        Map<Integer, Integer> freq = new HashMap<>();
        for (int d : sorted) freq.merge(d, 1, Integer::sum);
        List<Integer> counts = new ArrayList<>(freq.values());
        Collections.sort(counts, Collections.reverseOrder());

        if (counts.get(0) == 3) return 15;     
        if (isStraight3(sorted)) return 12;    
        if (counts.get(0) == 2) return 8;      
        return 5;                             
    }

    private String lineName(int[] line) {
        int[] sorted = line.clone();
        Arrays.sort(sorted);

        Map<Integer, Integer> freq = new HashMap<>();
        for (int d : sorted) freq.merge(d, 1, Integer::sum);
        List<Integer> counts = new ArrayList<>(freq.values());
        Collections.sort(counts, Collections.reverseOrder());

        if (counts.get(0) == 3) return "Three of a Kind";
        if (isStraight3(sorted)) return "Straight";
        if (counts.get(0) == 2) return "Pair";
        return "All Different";
    }

    private boolean isStraight3(int[] sorted) {
        return (sorted[1] == sorted[0] + 1) && (sorted[2] == sorted[1] + 1);
    }
}
