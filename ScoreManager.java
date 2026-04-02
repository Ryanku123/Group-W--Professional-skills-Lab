import java.util.*;

//The ScoreManager class acts as the person who manages leaderboards for each game in the system. 
  //responsibility 
     //1. collect of highest scores from all the registered users regarding a specific game. 
     //2. This class performs sorting and then it shows the leaderboard with rankings.
  //design
     //1. ScoreManager does not have its own storage for the scores. 
     //2. full list of objects of User from the UserManager and it makes a reading of values through 
     // the method getHighestScore for the game index when there is a demand. 
     // This is a very important issue for the prevention of duplication of data because 
     // the User stays as the single source of truth about the matter.

   //usage of the main program
      //the scoreManager can show the leaderboard by using the index of game and the name of game and all users.
          //The building and the printing of the ranked leaderboard for one specific game is done by the code. 
          //This involves the 0-based index which matches with the score arrays of User 
          //It also involves the name of game for the header and the list of all the users from UserManager. 
          //This method allows for a clear display of performance and competition and ranking. 
          //This structure maintains a balance of information across the system for the users.

public class ScoreManager {

    
     //Builds and prints a ranked leaderboard for one specific game.
     
       //@param gameIndex  0-based index of the game (matches User's score arrays)
       // @param gameName   display name shown in the header
       // @param allUsers   all registered User objects (from UserManager)
     
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
            if      (i == 0) medal = " Gold ";
            else if (i == 1) medal = " Silver ";
            else if (i == 2) medal = " Bronze ";
            System.out.printf("%-6s %-20s %d pts%s%n",
                    "#" + (i + 1), names.get(i), scores.get(i)[0], medal);
        }
        System.out.println("--------------------------------------");
    }
}
