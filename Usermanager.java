import java.io.*;
import java.util.*;

/**
 * UserManager.java
 *
 * Handles player registration and persistent storage.
 *
 * How it works:
 *   - All player data is stored in "players.txt" (CSV format).
 *   - On startup, loadFromFile() reads existing data so returning players
 *     keep their stats between sessions.
 *   - register() either creates a new User or retrieves the existing one.
 *   - saveToFile() is called after every game to persist the latest scores.
 *
 * File format (players.txt):
 *   # header comment line (skipped on load)
 *   username,totalGames,high0,high1,high2,high3,recent0,recent1,recent2,recent3,lastDateTime
 */
public class UserManager {
    private static final String FILE_PATH = "players.txt";

    // LinkedHashMap preserves insertion order so the file stays readable
    private Map<String, User> users = new LinkedHashMap<>();

    public UserManager() {
        loadFromFile();  // Load saved players when the program starts
    }

    /**
     * Registers a new player or returns the existing player with that username.
     * Returns null if the username is blank (caller should prompt again).
     */
    public User register(String username) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Username cannot be empty.");
            return null;
        }
        username = username.trim();

        // Returning player: load their existing data
        if (users.containsKey(username)) {
            System.out.println("Welcome back, " + username + "!");
            return users.get(username);
        }

        // New player: create and immediately save so the file stays up to date
        User newUser = new User(username);
        users.put(username, newUser);
        saveToFile();
        System.out.println("New user registered: " + username);
        return newUser;
    }

    /**
     * Writes all current player data to players.txt.
     * Called after register() and after every game score is recorded.
     */
    public void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            pw.println("# username,totalGames,high0,high1,high2,high3,recent0,recent1,recent2,recent3,lastDateTime");
            for (User u : users.values()) {
                StringBuilder sb = new StringBuilder();
                sb.append(u.getUsername()).append(",");
                sb.append(u.getTotalGamesPlayed()).append(",");
                for (int i = 0; i < User.NUM_GAMES; i++) sb.append(u.getHighestScore(i)).append(",");
                for (int i = 0; i < User.NUM_GAMES; i++) sb.append(u.getRecentScore(i)).append(",");
                sb.append(u.getLastPlayedDateTime());
                pw.println(sb.toString());
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not save player data. " + e.getMessage());
        }
    }

    /**
     * Returns all registered users as a List.
     * Used by ScoreManager to build leaderboards.
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Reads players.txt on startup and reconstructs all User objects.
     * Lines starting with '#' are treated as comments and skipped.
     * Malformed lines are silently ignored to prevent crashes.
     */
    private void loadFromFile() {
        File f = new File(FILE_PATH);
        if (!f.exists()) return;  // First run: no file yet, that is fine

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) continue;

                String[] parts = line.split(",", -1);
                // Expected columns: 1 username + 1 total + 4 highest + 4 recent + 1 datetime = 11
                if (parts.length < 11) continue;

                try {
                    String name = parts[0];
                    User u = new User(name);
                    u.setTotalGamesPlayed(Integer.parseInt(parts[1]));
                    for (int i = 0; i < User.NUM_GAMES; i++) u.setHighestScore(i, Integer.parseInt(parts[2 + i]));
                    for (int i = 0; i < User.NUM_GAMES; i++) u.setRecentScore(i, Integer.parseInt(parts[6 + i]));
                    u.setLastPlayedDateTime(parts[10]);
                    users.put(name, u);
                } catch (NumberFormatException ignore) {
                    // Skip corrupted lines without crashing
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not load player data.");
        }
    }
}
