import java.util.*;

// ScoreManager.java
// Shows leaderboard for a selected game
public class ScoreManager {

    public void showLeaderboard(int gameIndex, String gameName, List<User> allUsers) {
        List<UserScore> leaderboard = new ArrayList<>();

        for (User user : allUsers) {
            int score = user.getHighestScore(gameIndex);
            if (score > 0) {
                leaderboard.add(new UserScore(user.getUsername(), score));
            }
        }

        leaderboard.sort((a, b) -> Integer.compare(b.score, a.score));

        System.out.println("\n=== Leaderboard: " + gameName + " ===");

        if (leaderboard.isEmpty()) {
            System.out.println("No scores recorded yet.");
            return;
        }

        for (int i = 0; i < leaderboard.size(); i++) {
            UserScore entry = leaderboard.get(i);
            String medal = "";

            if (i == 0) {
                medal = "1st ";
            } else if (i == 1) {
                medal = "2nd";
            } else if (i == 2) {
                medal = "3rd ";
            }

            System.out.println((i + 1) + ". " + medal + entry.username + " - " + entry.score);
        }
    }

    private static class UserScore {
        String username;
        int score;

        UserScore(String username, int score) {
            this.username = username;
            this.score = score;
        }
    }
}
