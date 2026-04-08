import java.io.*;
import java.util.*;

// UserManager.java
// Handles player registration and persistent storage
public class UserManager {
    private static final String FILE_PATH = "players.txt";

    private Map<String, User> users = new LinkedHashMap<>();

    public UserManager() {
        loadFromFile();
    }

    public User register(String username) {
        username = username.trim();

        if (users.containsKey(username)) {
            System.out.println("Welcome back, " + username + "!");
            return users.get(username);
        }

        User newUser = new User(username);
        users.put(username, newUser);
        saveToFile();

        System.out.println("New player registered: " + username);
        return newUser;
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            writer.println("# username,totalGames,high0,high1,high2,high3,recent0,recent1,recent2,recent3");

            for (User user : users.values()) {
                writer.print(user.getUsername());
                writer.print("," + user.getTotalGamesPlayed());

                for (int i = 0; i < 4; i++) {
                    writer.print("," + user.getHighestScore(i));
                }

                for (int i = 0; i < 4; i++) {
                    writer.print("," + user.getRecentScore(i));
                }

                writer.println();
            }
        } catch (IOException e) {
            System.out.println("Error saving player data: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length < 10) {
                    continue;
                }

                String username = parts[0];
                int totalGames = Integer.parseInt(parts[1]);

                User user = new User(username);

                user.setTotalGamesPlayed(totalGames);

                for (int i = 0; i < 4; i++) {
                    user.setHighestScore(i, Integer.parseInt(parts[2 + i]));
                }

                for (int i = 0; i < 4; i++) {
                    user.setRecentScore(i, Integer.parseInt(parts[6 + i]));
                }

                users.put(username, user);
            }
        } catch (Exception e) {
            System.out.println("Error loading player data: " + e.getMessage());
        }
    }
}
