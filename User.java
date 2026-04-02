import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//User.java
 
 // Stores all data for a single registered player.
 //Tracks:
   //  1. username
   //  2. total number of games played (across all 4 games)
   //  3. highest score per game (array index 0~3)
   //  4. most recent score per game (array index 0~3)
   //  5. date/time of the last gameplay session
 
 // recordScore() is called by Main after every game to update stats.
 //printStats() displays a formatted summary to the console.
 
public class User {
    private String username;
    private int totalGamesPlayed;
    private int[] highestScore;        // one slot per game, index 0-3
    private int[] recentScore;         // one slot per game, index 0-3
    private String lastPlayedDateTime;

    public static final int NUM_GAMES = 4;

    public User(String username) {
        this.username = username;
        this.totalGamesPlayed = 0;
        this.highestScore = new int[NUM_GAMES];
        this.recentScore  = new int[NUM_GAMES];
        this.lastPlayedDateTime = "Never";
    }

    //
     // Called after every game session.
     // Increments total games played, updates recent score,
     // updates highest score if the new score beats the old record,
     // and records the current date/time.
     
    public void recordScore(int gameIndex, int score) {
        totalGamesPlayed++;
        recentScore[gameIndex] = score;
        if (score > highestScore[gameIndex]) {
            highestScore[gameIndex] = score;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        lastPlayedDateTime = LocalDateTime.now().format(fmt);
    }

    // --- Getters ---
    public String getUsername()             { return username; }
    public int    getTotalGamesPlayed()     { return totalGamesPlayed; }
    public int    getHighestScore(int idx)  { return highestScore[idx]; }
    public int    getRecentScore(int idx)   { return recentScore[idx]; }
    public String getLastPlayedDateTime()   { return lastPlayedDateTime; }

    // --- Setters (used only when loading saved data from players.txt) ---
    public void setTotalGamesPlayed(int n)        { totalGamesPlayed = n; }
    public void setHighestScore(int idx, int val) { highestScore[idx] = val; }
    public void setRecentScore(int idx, int val)  { recentScore[idx] = val; }
    public void setLastPlayedDateTime(String dt)  { lastPlayedDateTime = dt; }

    // Prints a formatted stat summary for this player to the console. 
    public void printStats(String[] gameNames) {
        System.out.println("\n===== Player Stats: " + username + " =====");
        System.out.println("Total games played : " + totalGamesPlayed);
        System.out.println("Last played        : " + lastPlayedDateTime);
        for (int i = 0; i < NUM_GAMES; i++) {
            System.out.printf("  %-32s | Best: %4d | Last: %4d%n",
                    gameNames[i], highestScore[i], recentScore[i]);
        }
    }
}
