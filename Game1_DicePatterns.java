import java.util.*;

public class Game1_DicePatterns implements Game {

    private static final String NAME = "Dice Patterns Challenge";
    private final Random rand = new Random();
    private final Scanner sc = new Scanner(System.in);

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int play(User user) {
        System.out.println("===============================");
        System.out.println("GAME 1: Dice Patterns Challenge");
        System.out.println("===============================");
        System.out.println("Roll 5 dice. Re-roll up to 2 times.");
        System.out.println("Score is based on the final pattern.");
        System.out.println();

        int[] dice = rollAll(5);
        printDice(dice);

        for (int reroll = 1; reroll <= 2; reroll++) {
            System.out.println();
            System.out.println("Re-roll attempt " + reroll + "/2. Enter dice positions to keep");
            System.out.println("(e.g. 1 3 5), or ENTER to re-roll all, or 'done' to stop:");

            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("done")) break;

            boolean[] keep = new boolean[5];
            if (!input.isEmpty()) {
                for (String tok : input.split("\\s+")) {
                    try {
                        int pos = Integer.parseInt(tok) - 1;
                        if (pos >= 0 && pos < 5) keep[pos] = true;
                    } catch (Exception ignored) {}
                }
            }

            reroll(dice, keep);
            printDice(dice);
        }

        System.out.println();
        System.out.println("Final dice: " + Arrays.toString(dice));
        String pattern = getPatternName(dice);
        int score = calculateScore(dice);
        System.out.println("Pattern: " + pattern + " => " + score + " pts");
        return score;
    }

    // ── formerly DiceRoller ──────────────────────────────────────────────────

    /** Roll 'count' fresh dice and return their values. */
    private int[] rollAll(int count) {
        int[] dice = new int[count];
        for (int i = 0; i < count; i++) {
            dice[i] = rand.nextInt(6) + 1;
        }
        return dice;
    }

    /** Re-roll every position that is NOT marked as kept. */
    private void reroll(int[] dice, boolean[] keep) {
        for (int i = 0; i < dice.length; i++) {
            if (!keep[i]) dice[i] = rand.nextInt(6) + 1;
        }
    }

    // ── formerly DicePrinter ─────────────────────────────────────────────────

    /** Print each die with its 1-based position label. */
    private void printDice(int[] dice) {
        System.out.print("Dice: ");
        for (int i = 0; i < dice.length; i++) {
            System.out.print("[" + (i + 1) + "]=" + dice[i] + "  ");
        }
        System.out.println();
    }

    // ── formerly DiceScorer ──────────────────────────────────────────────────

    /** Return the score for the final dice pattern. */
    private int calculateScore(int[] dice) {
        int[] sorted = dice.clone();
        Arrays.sort(sorted);

        Map<Integer, Integer> freq = new HashMap<>();
        for (int d : sorted) freq.merge(d, 1, Integer::sum);

        List<Integer> counts = new ArrayList<>(freq.values());
        Collections.sort(counts, Collections.reverseOrder());

        if (counts.get(0) == 5) return 50;  // Five of a Kind
        if (counts.get(0) == 4) return 40;  // Four of a Kind
        if (counts.get(0) == 3 && counts.size() > 1 && counts.get(1) == 2) return 35; // Full House
        if (isStraight(sorted))  return 30;  // Straight
        if (counts.get(0) == 3)  return 25;  // Three of a Kind
        long pairs = counts.stream().filter(c -> c == 2).count();
        if (pairs == 2) return 20;           // Two Pairs
        if (counts.get(0) == 2) return 10;   // One Pair
        return 0;                             // No Match
    }

    /** Return the pattern name for display. */
    private String getPatternName(int[] dice) {
        int[] sorted = dice.clone();
        Arrays.sort(sorted);

        Map<Integer, Integer> freq = new HashMap<>();
        for (int d : sorted) freq.merge(d, 1, Integer::sum);

        List<Integer> counts = new ArrayList<>(freq.values());
        Collections.sort(counts, Collections.reverseOrder());

        if (counts.get(0) == 5) return "Five of a Kind";
        if (counts.get(0) == 4) return "Four of a Kind";
        if (counts.get(0) == 3 && counts.size() > 1 && counts.get(1) == 2) return "Full House";
        if (isStraight(sorted))  return "Straight";
        if (counts.get(0) == 3)  return "Three of a Kind";
        long pairs = counts.stream().filter(c -> c == 2).count();
        if (pairs == 2) return "Two Pairs";
        if (counts.get(0) == 2) return "One Pair";
        return "No Match";
    }

    /** Check if 5 sorted dice values form a valid straight (1-5 or 2-6). */
    private boolean isStraight(int[] sorted) {
        if (sorted[0] != 1 && sorted[0] != 2) return false;
        for (int i = 1; i < sorted.length; i++) {
            if (sorted[i] != sorted[i - 1] + 1) return false;
        }
        return true;
    }
}
