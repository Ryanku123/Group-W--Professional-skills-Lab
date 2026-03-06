import java.util.Random;

public class GameD implements Game {
    private ScoreManager scoreManager;

    public GameD(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    @Override
    public void start(User user) {
        System.out.println("Starting Game D...");
        int score = new Random().nextInt(400);
        System.out.println("Your score: " + score);
        scoreManager.addScore("GameD", score);
    }

    @Override
    public void showScores() {
        scoreManager.showScores("GameD");
    }
}
