import java.util.*;

// Game2_DiceGrid.java
// 3x3 grid dice placement game
public class Game2_DiceGrid implements Game {
    private Scanner scanner = new Scanner(System.in);
    private Random random = new Random();

    @Override
    public String getName() {
        return "Dice Grid";
    }

    @Override
    public int play(User user) {
        System.out.println("\n=== " + getName() + " ===");
        System.out.println("Fill a 3x3 grid with dice rolls.");
        System.out.println("Each turn you roll one die and choose where to place it.");
        System.out.println("Rows, columns, and diagonals score points based on patterns.");

        int[][] grid = new int[3][3];

        // Fill grid with 9 dice placements
        for (int turn = 1; turn <= 9; turn++) {
            int roll = random.nextInt(6) + 1;
            System.out.println("\nTurn " + turn + ": You rolled " + roll);

            printGrid(grid);

            int row, col;
            while (true) {
                row = readInt("Choose row (1-3): ") - 1;
                col = readInt("Choose column (1-3): ") - 1;

                if (row < 0 || row > 2 || col < 0 || col > 2) {
                    System.out.println("Invalid position. Use numbers 1 to 3.");
                } else if (grid[row][col] != 0) {
                    System.out.println("That cell is already filled. Choose another one.");
                } else {
                    break;
                }
            }

            grid[row][col] = roll;
        }

        System.out.println("\nFinal Grid:");
        printGrid(grid);

        int totalScore = scoreGrid(grid);

        System.out.println("\n" + user.getUsername() + ", your score for " + getName() + ": " + totalScore);
        return totalScore;
    }

    private void printGrid(int[][] grid) {
        System.out.println();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j] == 0) {
                    System.out.print("[ ] ");
                } else {
                    System.out.print("[" + grid[i][j] + "] ");
                }
            }
            System.out.println();
        }
    }

    private int scoreGrid(int[][] grid) {
        int total = 0;

        // Rows
        for (int i = 0; i < 3; i++) {
            int[] line = {grid[i][0], grid[i][1], grid[i][2]};
            int lineScore = scoreLine(line);
            System.out.println("Row " + (i + 1) + ": " + Arrays.toString(line)
                    + " -> " + lineName(line) + " = " + lineScore + " points");
            total += lineScore;
        }

        // Columns
        for (int j = 0; j < 3; j++) {
            int[] line = {grid[0][j], grid[1][j], grid[2][j]};
            int lineScore = scoreLine(line);
            System.out.println("Column " + (j + 1) + ": " + Arrays.toString(line)
                    + " -> " + lineName(line) + " = " + lineScore + " points");
            total += lineScore;
        }

        // Main diagonal
        int[] diag1 = {grid[0][0], grid[1][1], grid[2][2]};
        int diag1Score = scoreLine(diag1);
        System.out.println("Diagonal 1: " + Arrays.toString(diag1)
                + " -> " + lineName(diag1) + " = " + diag1Score + " points");
        total += diag1Score;

        // Other diagonal
        int[] diag2 = {grid[0][2], grid[1][1], grid[2][0]};
        int diag2Score = scoreLine(diag2);
        System.out.println("Diagonal 2: " + Arrays.toString(diag2)
                + " -> " + lineName(diag2) + " = " + diag2Score + " points");
        total += diag2Score;

        System.out.println("\nTotal score: " + total);
        return total;
    }

    private int scoreLine(int[] line) {
        int[] sorted = line.clone();
        Arrays.sort(sorted);

        // Three of a kind
        if (sorted[0] == sorted[1] && sorted[1] == sorted[2]) {
            return 30;
        }

        // Straight
        if (isStraight3(sorted)) {
            return 20;
        }

        // Pair
        if (sorted[0] == sorted[1] || sorted[1] == sorted[2]) {
            return 10;
        }

        // All different
        return 5;
    }

    private String lineName(int[] line) {
        int[] sorted = line.clone();
        Arrays.sort(sorted);

        if (sorted[0] == sorted[1] && sorted[1] == sorted[2]) {
            return "Three of a Kind";
        }

        if (isStraight3(sorted)) {
            return "Straight";
        }

        if (sorted[0] == sorted[1] || sorted[1] == sorted[2]) {
            return "Pair";
        }

        return "All Different";
    }

    private boolean isStraight3(int[] sorted) {
        return sorted[0] + 1 == sorted[1] && sorted[1] + 1 == sorted[2];
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
