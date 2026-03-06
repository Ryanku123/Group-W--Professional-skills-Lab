import java.util.Random;

public class GameC implements Game {
    private ScoreManager scoreManager;

    public GameC(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    @Override
    public void start(User user) {
        System.out.println("Starting Game C...");
        int score = new Random().nextInt(300);
        System.out.println("Your score: " + score);
        scoreManager.addScore("GameC", score);
    }

    @Override
    public void showScores() {
        scoreManager.showScores("GameC");
    }
}

