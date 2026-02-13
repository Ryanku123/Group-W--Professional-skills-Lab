import java.util.Random;

public class GameB implements Game {
    private ScoreManager scoreManager;

    public GameB(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    @Override
    public void start(User user) {
        System.out.println("Starting Game B...");
        int score = new Random().nextInt(200);
        System.out.println("Your score: " + score);
        scoreManager.addScore("GameB", score);
    }

    @Override
    public void showScores() {
        scoreManager.showScores("GameB");
    }
}
