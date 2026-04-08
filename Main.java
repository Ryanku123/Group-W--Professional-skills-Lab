import java.util.Scanner;


//The structure of the software includes a main menu where the user is able to make a selection of a game and its appropriate sub-menu. 
 // featuree
    // 1.When the exit from a game occurs, the system makes a call to the record score function and the save to file function for the user manager to achieve the insurance of stats being saved immediately after the session of gaming. 
    // 2.players have the ability to switch between accounts without a requirement to restart the whole program.

//Regarding the architecture of the code, a design pattern which is called array of interfaces was implemented. All four of the game objects are stored in a single array of the game type. The loop for the menu can call the play method for any index in the array and it is not important which specific subclass of game is located at that index. If there is a need for adding a fifth game, this only requires the addition of one line in the declaration of the array.
//The validation of input is also a very important part of the project. There is a method for reading integers that uses a try and catch block to process the parsing of the integer. If an error is happening during this process, the method returns a value of negative one. Each part of the menu then performs a check to see if the value is negative one and provides a response by using a default message. Because of this, any invalid input is managed by the code and it never causes the stopping of the program because of an exception. This ensures that the user experience is staying consistent and stable.
public class Main {
    private static final String[] GAME_NAMES = {
        "Dice Patterns Challenge",
        "Dice Grid Puzzle",
        "Dice Codebreaker",
        "Dice Blackjack"
    };

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Core manager objects is created once and reused throughout the session
        UserManager userManager = new UserManager();  // Loads players.txt on creation
        ScoreManager scoreManager = new ScoreManager();

        // each game will handles its own scoring internally and returns an int score.
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

    
     //Displays the sub-menu for a specific game.
      // Options: Play, view best score, or go back but i didn't unify yet for all games
      // After playing, immediately saves the updated stats to file.
     
        //@param sc          shared Scanner for console input
        //@param game        the selected Game implementation
        // @param gameIndex   0-based index used to reference the correct stat slot in User
        // @param user        the currently active player
        // @param userManager used to persist data after every game
     
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

    
     // Leaderboard selection menu — user can pick which game's leaderboard to view.
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

    
     // Safe integer reader — returns -1 if the input is not a valid integer.
     // Prevents NumberFormatException from crashing the menu loops.
     
    private static int readInt(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
