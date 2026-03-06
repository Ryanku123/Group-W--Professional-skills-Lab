game 4


요약

Mohammad Zaid Ibrahim ALKURDI

​
Ryan KU​
import java.util.Random;
import java.util.Scanner;

/**
 * Dice Blackjack - Blackjack rules played with dice instead of cards.
 *
 * Dice Values (replaces card values):
 *   - Each die is a standard 6-sided die (1-6)
 *   - A roll of 1 can count as 1 or 11 (like an Ace)
 *   - A roll of 6 counts as 10 (like a face card)
 *   - Rolls 2-5 count as their face value
 *
 * Rules:
 *   - Goal: get as close to 21 as possible without going over
 *   - Player rolls first, then decides to Roll Again (Hit) or Stay (Stand)
 *   - Dealer must roll again if total is 16 or less, and stay on 17+
 *   - Blackjack: rolling exactly 21 on the first two dice
 *   - Bust: total exceeds 21
 */
public class DiceBlackjack {

    static final int TARGET = 21;
    static final int DEALER_STAND = 17;
    static final Random random = new Random();
    static final Scanner scanner = new Scanner(System.in);

    // Player balance
    static int balance = 100;

    public static void main(String[] args) {
        printBanner();
        System.out.println("Welcome to Dice Blackjack!");
        System.out.println("Rules:");
        System.out.println("  - Roll of 1 = 1 or 11 (like an Ace)");
        System.out.println("  - Roll of 6 = 10 (like a Face Card)");
        System.out.println("  - Rolls 2-5 = face value");
        System.out.println("  - Dealer stands on 17+");
        System.out.println("  - Blackjack pays 2.5x your bet!");
        System.out.println("  - Starting balance: $" + balance);
        System.out.println();

        boolean playing = true;
        while (playing) {
            if (balance <= 0) {
                System.out.println("You're out of money! Game over.");
                break;
            }

            int bet = placeBet();
            GameResult result = playRound();
            balance = applyResult(balance, bet, result);

            System.out.println("\n💰 Balance: $" + balance);
            System.out.println();
            System.out.print("Play again? (y/n): ");
            String again = scanner.nextLine().trim().toLowerCase();
            playing = again.equals("y") || again.equals("yes");
        }

        System.out.println("\nThanks for playing! Final balance: $" + balance);
        scanner.close();
    }

    // -------------------------------------------------------------------------
    // Round logic
    // -------------------------------------------------------------------------

    static GameResult playRound() {
        System.out.println("\n========== NEW ROUND ==========");

        // --- Initial rolls ---
        int[] playerRolls = { rollDie(), rollDie() };
        int playerTotal = calculateTotal(playerRolls[0], playerRolls[1]);

        System.out.println("\n🎲 Your dice: [" + playerRolls[0] + "] [" + playerRolls[1] + "]"
                + "  →  total: " + playerTotal);

        // Dealer shows one die
        int dealerVisible = rollDie();
        int dealerHidden = rollDie();
        System.out.println("🎲 Dealer shows: [" + dealerVisible + "] [?]");

        // Check player blackjack
        if (playerTotal == TARGET) {
            // Reveal dealer
            int dealerTotal = calculateTotal(dealerVisible, dealerHidden);
            System.out.println("\n🃏 BLACKJACK! You rolled 21!");
            System.out.println("Dealer reveals: [" + dealerVisible + "] [" + dealerHidden + "]  →  total: " + dealerTotal);
            if (dealerTotal == TARGET) {
                System.out.println("Dealer also has Blackjack. It's a PUSH!");
                return GameResult.PUSH;
            }
            System.out.println("You win with Blackjack!");
            return GameResult.BLACKJACK;
        }

        // --- Player's turn ---
        int playerFinal = playerTurn(playerTotal);
        if (playerFinal > TARGET) {
            System.out.println("💥 BUST! You went over 21.");
            return GameResult.PLAYER_BUST;
        }

        // --- Dealer's turn ---
        System.out.println("\n--- Dealer's Turn ---");
        System.out.println("Dealer reveals: [" + dealerVisible + "] [" + dealerHidden + "]");
        int dealerFinal = dealerTurn(dealerVisible, dealerHidden);

        // --- Determine winner ---
        return determineWinner(playerFinal, dealerFinal);
    }

    static int playerTurn(int total) {
        while (true) {
            System.out.print("\nRoll again (hit) or stay? (h/s): ");
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("s") || choice.equals("stay")) {
                System.out.println("You stay with " + total + ".");
                return total;
            } else if (choice.equals("h") || choice.equals("hit")) {
                int die = rollDie();
                int dieValue = dieToValue(die);
                // Check if 1 should be 11
                if (die == 1 && total + 11 <= TARGET) {
                    dieValue = 11;
                }
                total += dieValue;
                System.out.println("🎲 You rolled: [" + die + "] (+" + dieValue + ")  →  total: " + total);
                if (total > TARGET) {
                    return total; // bust
                }
                if (total == TARGET) {
                    System.out.println("✨ Exactly 21!");
                    return total;
                }
            } else {
                System.out.println("Please enter 'h' to hit or 's' to stay.");
            }
        }
    }

    static int dealerTurn(int visible, int hidden) {
        int total = calculateTotal(visible, hidden);
        System.out.println("Dealer total: " + total);

        while (total <= DEALER_STAND - 1) {
            System.out.println("Dealer must roll again (total ≤ 16)...");
            int die = rollDie();
            int dieValue = dieToValue(die);
            if (die == 1 && total + 11 <= TARGET) {
                dieValue = 11;
            }
            total += dieValue;
            System.out.println("🎲 Dealer rolled: [" + die + "] (+" + dieValue + ")  →  total: " + total);
        }

        if (total > TARGET) {
            System.out.println("💥 Dealer BUSTS with " + total + "!");
        } else {
            System.out.println("Dealer stays with " + total + ".");
        }
        return total;
    }

    // -------------------------------------------------------------------------
    // Dice helpers
    // -------------------------------------------------------------------------

    static int rollDie() {
        return random.nextInt(6) + 1;
    }

    static int dieToValue(int die) {
        if (die == 6) return 10;
        return die; // 1-5 face value; ace (1) logic handled at call site
    }

    /**
     * Calculate optimal total for two starting dice, treating 1 as 11 if beneficial.
     */
    static int calculateTotal(int d1, int d2) {
        int v1 = dieToValue(d1);
        int v2 = dieToValue(d2);

        // Try treating a 1 as 11
        if (d1 == 1 && v2 + 11 <= TARGET) v1 = 11;
        else if (d2 == 1 && v1 + 11 <= TARGET) v2 = 11;

        return v1 + v2;
    }

    // -------------------------------------------------------------------------
    // Betting and results
    // -------------------------------------------------------------------------

    static int placeBet() {
        while (true) {
            System.out.print("Your balance: $" + balance + "  —  Enter your bet: $");
            String input = scanner.nextLine().trim();
            try {
                int bet = Integer.parseInt(input);
                if (bet <= 0) {
                    System.out.println("Bet must be at least $1.");
                } else if (bet > balance) {
                    System.out.println("You can't bet more than your balance.");
                } else {
                    return bet;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    static int applyResult(int bal, int bet, GameResult result) {
        switch (result) {
            case BLACKJACK:
                int winnings = (int)(bet * 2.5);
                System.out.println("🏆 BLACKJACK! You win $" + winnings + "!");
                return bal + winnings;
            case PLAYER_WIN:
                System.out.println("🏆 You WIN $" + bet + "!");
                return bal + bet;
            case PUSH:
                System.out.println("🤝 PUSH — your bet is returned.");
                return bal;
            case PLAYER_BUST:
            case DEALER_WIN:
                System.out.println("😞 You lose $" + bet + ".");
                return bal - bet;
            default:
                return bal;
        }
    }

    static GameResult determineWinner(int playerTotal, int dealerTotal) {
        System.out.println("\n--- Result ---");
        System.out.println("Your total:   " + playerTotal);
        System.out.println("Dealer total: " + (dealerTotal > TARGET ? dealerTotal + " (BUST)" : dealerTotal));

        if (dealerTotal > TARGET) {
            return GameResult.PLAYER_WIN;
        }
        if (playerTotal > dealerTotal) {
            return GameResult.PLAYER_WIN;
        }
        if (playerTotal == dealerTotal) {
            return GameResult.PUSH;
        }
        return GameResult.DEALER_WIN;
    }

    // -------------------------------------------------------------------------
    // Banner
    // -------------------------------------------------------------------------

    static void printBanner() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║        🎲  DICE  BLACKJACK  🎲       ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    // -------------------------------------------------------------------------
    // Enum
    // -------------------------------------------------------------------------

    enum GameResult {
        BLACKJACK,
        PLAYER_WIN,
        DEALER_WIN,
        PUSH,
        PLAYER_BUST
    }
}
