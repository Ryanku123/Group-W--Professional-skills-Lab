import java.util.Random;

public class GameA implements Game {
    private ScoreManager scoreManager;

    public GameA(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    @Override
    public void start(User user) {
        System.out.println("Starting Game A...");
        int score = new Random().nextInt(100);
        System.out.println("Your score: " + score);
        scoreManager.addScore("GameA", score);
    }

    @Override
    public void showScores() {
        scoreManager.showScores("GameA");
    }
}
