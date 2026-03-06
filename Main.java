import java.util.Scanner;

/**
 * Main.java — Program Entry Point
 *
 * Responsibilities:
 *   1. Start the program and initialise core objects (UserManager, all 4 games).
 *   2. Handle player registration / login via UserManager.
 *   3. Display the main menu and route to the chosen game's sub-menu.
 *   4. After each game, call user.recordScore() and userManager.saveToFile()
 *      to persist stats immediately.
 *   5. Support switching between players without restarting the program.
 *
 * Design pattern used — "Array of interfaces":
 *   All 4 game objects are stored in a Game[] array.
 *   This means the menu loop can call games[i].play(user) uniformly
 *   without knowing (or caring) which specific game class is behind index i.
 *   Adding a 5th game only requires adding one line to the array.
 *
 * Input validation:
 *   readInt() wraps Integer.parseInt in a try/catch and returns -1 on failure.
 *   Every menu switch/if then handles -1 gracefully with a default message,
 *   so invalid input never crashes the program.
 */
public class Main {

    /** Display names used in menus and stat screens — must match game order in the array. */
    private static final String[] GAME_NAMES = {
        "Dice Patterns Challenge",
        "Dice Grid Puzzle",
        "Dice Codebreaker",
        "Dice Blackjack"
    };

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Core manager objects — created once and reused throughout the session
        UserManager userManager = new UserManager();  // Loads players.txt on creation
        ScoreManager scoreManager = new ScoreManager();

        // All 4 games share a single ScoreManager is NOT needed here —
        // each game handles its own scoring internally and returns an int score.
        Game[] games = {
            new Game1_DicePatterns(),
            new Game2_DiceGrid(),
            new Game3_DiceCodebreaker(),
            new Game4_DiceBlackjack()
        };

        // --- Step 1: Registration / Login ---
        // Loop until a valid (non-null) User is returned by register()
        User currentUser = null;
        while (currentUser == null) {
            System.out.print("\nEnter your username (new or existing): ");
            String input = sc.nextLine().trim();
            currentUser = userManager.register(input);
        }

        // --- Step 2: Main Menu Loop ---
        boolean running = true;
        while (running) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("Player: " + currentUser.getUsername());
            System.out.println("--------------------------------");
            for (int i = 0; i < games.length; i++) {
                System.out.println((i + 1) + ". " + games[i].getName());
            }
            System.out.println("5. View My Stats");
            System.out.println("6. View Leaderboard");
            System.out.println("7. Switch Player");
            System.out.println("8. Exit");
            System.out.print("Choose: ");

            int choice = readInt(sc);
            switch (choice) {
                case 1: case 2: case 3: case 4:
                    // Route to the game sub-menu; index is choice-1 (0-based)
                    runGameMenu(sc, games[choice - 1], choice - 1, currentUser, userManager, scoreManager);
                    break;
                case 5:
                    currentUser.printStats(GAME_NAMES);
                    break;
                case 6:
                    showLeaderboardMenu(sc, scoreManager, userManager, GAME_NAMES);
                    break;
                case 7:
                    // Switch to a different player without exiting
                    currentUser = null;
                    while (currentUser == null) {
                        System.out.print("Enter username: ");
                        currentUser = userManager.register(sc.nextLine().trim());
                    }
                    break;
                case 8:
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1-8.");
            }
        }
        sc.close();
    }

    /**
     * Displays the sub-menu for a specific game.
     * Options: Play, view best score, or go back.
     * After playing, immediately saves the updated stats to file.
     *
     * @param sc          shared Scanner for console input
     * @param game        the selected Game implementation
     * @param gameIndex   0-based index used to reference the correct stat slot in User
     * @param user        the currently active player
     * @param userManager used to persist data after every game
     */
    private static void runGameMenu(Scanner sc, Game game, int gameIndex,
                                    User user, UserManager userManager, ScoreManager scoreManager) {
        while (true) {
            System.out.println("\n--- " + game.getName() + " ---");
            System.out.println("1. Play");
            System.out.println("2. My best score: " + user.getHighestScore(gameIndex));
            System.out.println("3. Leaderboard");
            System.out.println("4. Back to Main Menu");
            System.out.print("Choose: ");

            int choice = readInt(sc);
            if (choice == 1) {
                int score = game.play(user);          // Run the game, get the score
                user.recordScore(gameIndex, score);   // Update user stats in memory
                userManager.saveToFile();             // Persist to players.txt immediately
                System.out.println("\n>> Score recorded: " + score + " pts");
            } else if (choice == 2) {
                System.out.println("Best: " + user.getHighestScore(gameIndex)
                        + "  |  Last: " + user.getRecentScore(gameIndex));
            } else if (choice == 3) {
                scoreManager.showLeaderboard(gameIndex, game.getName(), userManager.getAllUsers());
            } else if (choice == 4) {
                break;  // Return to main menu
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Leaderboard selection menu — lets the user pick which game's leaderboard to view.
     */
    private static void showLeaderboardMenu(Scanner sc, ScoreManager scoreManager,
                                            UserManager userManager, String[] gameNames) {
        while (true) {
            System.out.println("\n===== Leaderboard =====");
            for (int i = 0; i < gameNames.length; i++) {
                System.out.println((i + 1) + ". " + gameNames[i]);
            }
            System.out.println("5. Back");
            System.out.print("Choose a game: ");

            int choice = readInt(sc);
            if (choice >= 1 && choice <= gameNames.length) {
                scoreManager.showLeaderboard(choice - 1, gameNames[choice - 1], userManager.getAllUsers());
            } else if (choice == 5) {
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    /**
     * Safe integer reader — returns -1 if the input is not a valid integer.
     * Prevents NumberFormatException from crashing the menu loops.
     */
    private static int readInt(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
