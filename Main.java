import java.util.*;

public class Main {
    private static final String[] GAME_NAMES = {
        "Dice Patterns",
        "Dice Grid",
        "Dice Codebreaker",
        "Dice Blackjack"
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserManager userManager = new UserManager();

        Game[] games = {
            new Game1_DicePatterns(),
            new Game2_DiceGrid(),
            new Game3_DiceCodebreaker(),
            new Game4_DiceBlackjack()
        };

        System.out.println("=================================");
        System.out.println("     WELCOME TO DICE ARCADE");
        System.out.println("=================================");

        User currentUser = null;

        while (currentUser == null) {
            System.out.print("Enter your username to login/register: ");
            String username = scanner.nextLine().trim();

            if (username.isEmpty()) {
                System.out.println("Username cannot be empty.");
            } else {
                currentUser = userManager.register(username);
            }
        }

        System.out.println("\nWelcome, " + currentUser.getUsername() + "!");
        runMainMenu(scanner, currentUser, games, userManager);
    }

    private static void runMainMenu(Scanner scanner, User user, Game[] games, UserManager userManager) {
        while (true) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Play a Game");
            System.out.println("2. Show Leaderboard");
            System.out.println("3. Show My Scores");
            System.out.println("0. Exit");
            System.out.println("================================");

            int choice = readInt(scanner, "Choose an option: ");

            switch (choice) {
                case 1:
                    runGameMenu(scanner, user, games, userManager);
                    break;
                case 2:
                    showLeaderboardMenu(scanner, userManager);
                    break;
                case 3:
                    showUserScores(user);
                    break;
                case 0:
                    userManager.saveToFile();
                    System.out.println("Goodbye, " + user.getUsername() + "!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void runGameMenu(Scanner scanner, User user, Game[] games, UserManager userManager) {
        while (true) {
            System.out.println("\n========== GAME MENU ==========");
            for (int i = 0; i < games.length; i++) {
                System.out.println((i + 1) + ". " + games[i].getName());
            }
            System.out.println("0. Back");
            System.out.println("================================");

            int choice = readInt(scanner, "Choose a game: ");

            if (choice == 0) {
                return;
            }

            if (choice < 1 || choice > games.length) {
                System.out.println("Invalid choice. Try again.");
                continue;
            }

            Game selectedGame = games[choice - 1];
            int score = selectedGame.play(user);

            user.recordScore(choice - 1, score);
            userManager.saveToFile();

            System.out.println("Score saved successfully.");
        }
    }

    private static void showLeaderboardMenu(Scanner scanner, UserManager userManager) {
        while (true) {
            System.out.println("\n======= LEADERBOARD MENU =======");
            for (int i = 0; i < GAME_NAMES.length; i++) {
                System.out.println((i + 1) + ". " + GAME_NAMES[i]);
            }
            System.out.println("0. Back");
            System.out.println("================================");

            int choice = readInt(scanner, "Choose a leaderboard: ");

            if (choice == 0) {
                return;
            }

            if (choice < 1 || choice > GAME_NAMES.length) {
                System.out.println("Invalid choice. Try again.");
                continue;
            }

            ScoreManager scoreManager = new ScoreManager();
            scoreManager.showLeaderboard(
                choice - 1,
                GAME_NAMES[choice - 1],
                userManager.getAllUsers()
            );
        }
    }

    private static void showUserScores(User user) {
        System.out.println("\n========== MY SCORES ==========");
        System.out.println("Player: " + user.getUsername());

        for (int i = 0; i < GAME_NAMES.length; i++) {
            System.out.println(
                (i + 1) + ". " + GAME_NAMES[i]
                + " | Highest: " + user.getHighestScore(i)
                + " | Most Recent: " + user.getRecentScore(i)
            );
        }

        System.out.println("Total games played: " + user.getTotalGamesPlayed());
        System.out.println("================================");
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
