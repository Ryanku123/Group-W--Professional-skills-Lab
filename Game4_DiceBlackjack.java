import java.util.Random;
import java.util.Scanner;

/**
 * Game4_DiceBlackjack.java — Dice Blackjack (Team's Custom Game)
 *
 * Dice Values (replaces card values):
 *   - Roll of 1 = 1 or 11 (like an Ace)
 *   - Roll of 6 = 10      (like a Face Card)
 *   - Rolls 2-5 = face value
 *
 * Rules:
 *   - Goal: get as close to 21 as possible without going over
 *   - Player hits (h) or stays (s) each turn
 *   - Dealer must hit on 16 or less, stands on 17+
 *   - Blackjack: exactly 21 on the first two dice
 *   - Bust: total exceeds 21
 *
 * Scoring:
 *   - Blackjack (21 on first 2 dice) = 150 pts
 *   - Win                            = 100 pts
 *   - Push (tie)                     =  50 pts
 *   - Bust or Loss                   =   0 pts
 */
public class Game4_DiceBlackjack implements Game {

    private static final int TARGET      = 21;
    private static final int DEALER_STAND = 17;

    private final Random  random  = new Random();
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String getName() {
        return "Dice Blackjack";
    }

    @Override
    public int play(User user) {
        printBanner();
        System.out.println("Rules:");
        System.out.println("  - Roll of 1 = 1 or 11 (like an Ace)");
        System.out.println("  - Roll of 6 = 10 (like a Face Card)");
        System.out.println("  - Rolls 2-5 = face value");
        System.out.println("  - Dealer stands on 17+");
        System.out.println();

        GameResult result = playRound();
        int score = resultToScore(result);
        System.out.println("Score earned: " + score + " pts");
        return score;
    }

    // ── Round logic ───────────────────────────────────────────────────────────

    private GameResult playRound() {
        System.out.println("\n========== NEW ROUND ==========");

        // Initial rolls
        int[] playerRolls = { rollDie(), rollDie() };
        int playerTotal = calculateTotal(playerRolls[0], playerRolls[1]);

        System.out.println("\nYour dice: [" + playerRolls[0] + "] [" + playerRolls[1] + "]"
                + "  =>  total: " + playerTotal);

        // Dealer shows one die, hides the other
        int dealerVisible = rollDie();
        int dealerHidden  = rollDie();
        System.out.println("Dealer shows: [" + dealerVisible + "] [?]");

        // Check player blackjack (21 on first two dice)
        if (playerTotal == TARGET) {
            int dealerTotal = calculateTotal(dealerVisible, dealerHidden);
            System.out.println("\nBLACKJACK! You rolled 21!");
            System.out.println("Dealer reveals: [" + dealerVisible + "] [" + dealerHidden
                    + "]  =>  total: " + dealerTotal);
            if (dealerTotal == TARGET) {
                System.out.println("Dealer also has Blackjack. It's a PUSH!");
                return GameResult.PUSH;
            }
            System.out.println("You win with Blackjack!");
            return GameResult.BLACKJACK;
        }

        // Player's turn
        int playerFinal = playerTurn(playerTotal);
        if (playerFinal > TARGET) {
            System.out.println("BUST! You went over 21.");
            return GameResult.PLAYER_BUST;
        }

        // Dealer's turn
        System.out.println("\n--- Dealer's Turn ---");
        System.out.println("Dealer reveals: [" + dealerVisible + "] [" + dealerHidden + "]");
        int dealerFinal = dealerTurn(dealerVisible, dealerHidden);

        return determineWinner(playerFinal, dealerFinal);
    }

    private int playerTurn(int total) {
        while (true) {
            System.out.print("\nRoll again (hit) or stay? (h/s): ");
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("s") || choice.equals("stay")) {
                System.out.println("You stay with " + total + ".");
                return total;

            } else if (choice.equals("h") || choice.equals("hit")) {
                int die      = rollDie();
                int dieValue = dieToValue(die);
                // Treat 1 as 11 if it doesn't bust
                if (die == 1 && total + 11 <= TARGET) dieValue = 11;

                total += dieValue;
                System.out.println("You rolled: [" + die + "] (+" + dieValue + ")  =>  total: " + total);

                if (total > TARGET) return total;  // bust
                if (total == TARGET) {
                    System.out.println("Exactly 21!");
                    return total;
                }
            } else {
                System.out.println("Please enter 'h' to hit or 's' to stay.");
            }
        }
    }

    private int dealerTurn(int visible, int hidden) {
        int total = calculateTotal(visible, hidden);
        System.out.println("Dealer total: " + total);

        while (total < DEALER_STAND) {
            System.out.println("Dealer must roll again (total <= 16)...");
            int die      = rollDie();
            int dieValue = dieToValue(die);
            if (die == 1 && total + 11 <= TARGET) dieValue = 11;

            total += dieValue;
            System.out.println("Dealer rolled: [" + die + "] (+" + dieValue + ")  =>  total: " + total);
        }

        if (total > TARGET) {
            System.out.println("Dealer BUSTS with " + total + "!");
        } else {
            System.out.println("Dealer stays with " + total + ".");
        }
        return total;
    }

    // ── Determine winner ──────────────────────────────────────────────────────

    private GameResult determineWinner(int playerTotal, int dealerTotal) {
        System.out.println("\n--- Result ---");
        System.out.println("Your total  : " + playerTotal);
        System.out.println("Dealer total: " + (dealerTotal > TARGET ? dealerTotal + " (BUST)" : dealerTotal));

        if (dealerTotal > TARGET)          return GameResult.PLAYER_WIN;
        if (playerTotal > dealerTotal)     return GameResult.PLAYER_WIN;
        if (playerTotal == dealerTotal)    return GameResult.PUSH;
        return GameResult.DEALER_WIN;
    }

    // ── Score conversion ──────────────────────────────────────────────────────

    private int resultToScore(GameResult result) {
        switch (result) {
            case BLACKJACK:   System.out.println("BLACKJACK! Maximum score!"); return 150;
            case PLAYER_WIN:  System.out.println("You win!");                  return 100;
            case PUSH:        System.out.println("Push — it's a tie!");        return 50;
            default:          System.out.println("Better luck next time!");    return 0;
        }
    }

    // ── Dice helpers ──────────────────────────────────────────────────────────

    private int rollDie() {
        return random.nextInt(6) + 1;
    }

    /** Roll of 6 = 10 (face card), rolls 1-5 = face value. Ace (1) logic handled at call site. */
    private int dieToValue(int die) {
        if (die == 6) return 10;
        return die;
    }

    /** Calculate optimal total for two starting dice, treating 1 as 11 if beneficial. */
    private int calculateTotal(int d1, int d2) {
        int v1 = dieToValue(d1);
        int v2 = dieToValue(d2);
        if      (d1 == 1 && v2 + 11 <= TARGET) v1 = 11;
        else if (d2 == 1 && v1 + 11 <= TARGET) v2 = 11;
        return v1 + v2;
    }

    // ── Banner ────────────────────────────────────────────────────────────────

    private void printBanner() {
        System.out.println("==============================");
        System.out.println("  GAME 4: Dice Blackjack");
        System.out.println("==============================");
    }

    // ── Enum ──────────────────────────────────────────────────────────────────

    private enum GameResult {
        BLACKJACK,
        PLAYER_WIN,
        DEALER_WIN,
        PUSH,
        PLAYER_BUST
    }
}
