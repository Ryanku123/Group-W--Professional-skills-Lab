import java.util.*;

/**
 * ScoreManager.java — Per-Game Leaderboard Manager
 *
 * Responsibilities:
 *   - Collects the highest score of every registered user for each game.
 *   - Sorts and displays a ranked leaderboard per game.
 *
 * Design:
 *   ScoreManager does NOT store scores itself.
 *   It receives the full list of User objects from UserManager and reads
 *   their getHighestScore(gameIndex) values on demand.
 *   This avoids data duplication — User is still the single source of truth.
 *
 * Usage in Main:
 *   scoreManager.showLeaderboard(gameIndex, gameName, allUsers);
 */
public class ScoreManager {

    /**
     * Builds and prints a ranked leaderboard for one specific game.
     *
     * @param gameIndex  0-based index of the game (matches User's score arrays)
     * @param gameName   display name shown in the header
     * @param allUsers   all registered User objects (from UserManager)
     */
    public void showLeaderboard(int gameIndex, String gameName, List<User> allUsers) {
        if (allUsers.isEmpty()) {
            System.out.println("No players registered yet.");
            return;
        }

        // Copy users into a list of entries (username + their best score for this game)
        List<int[]>    scores = new ArrayList<>();
        List<String>   names  = new ArrayList<>();

        for (User u : allUsers) {
            names.add(u.getUsername());
            scores.add(new int[]{ u.getHighestScore(gameIndex) });
        }

        // Bubble sort: descending by best score
        int n = scores.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (scores.get(i)[0] < scores.get(j)[0]) {
                    // Swap scores
                    int[] tmpScore = scores.get(i);
                    scores.set(i, scores.get(j));
                    scores.set(j, tmpScore);
                    // Swap names in sync
                    String tmpName = names.get(i);
                    names.set(i, names.get(j));
                    names.set(j, tmpName);
                }
            }
        }

        // Print ranked leaderboard
        System.out.println("\n===== Leaderboard: " + gameName + " =====");
        System.out.printf("%-6s %-20s %s%n", "Rank", "Player", "Best Score");
        System.out.println("--------------------------------------");
        for (int i = 0; i < n; i++) {
            String medal = "";
            if      (i == 0) medal = " 🥇";
            else if (i == 1) medal = " 🥈";
            else if (i == 2) medal = " 🥉";
            System.out.printf("%-6s %-20s %d pts%s%n",
                    "#" + (i + 1), names.get(i), scores.get(i)[0], medal);
        }
        System.out.println("--------------------------------------");
    }
}
