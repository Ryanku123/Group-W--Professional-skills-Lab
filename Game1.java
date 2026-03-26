import java.util.*;

// Game 1: Dice Patterns Challenge
// tried to keep this readable but not too "perfect"
public class Game1_DicePatterns implements Game
{
    private static final String NAME = "Dice Patterns Challenge";

    // random + scanner
    private Random rng = new Random();
    private Scanner in = new Scanner(System.in);


    @Override
    public String getName() {
        return NAME;
    }


    @Override
    public int play(User user)
    {
        System.out.println("  ==============================");
        System.out.println("    GAME 1: Dice Patterns Challenge");
        System.out.println("  ==============================");
        System.out.println("  Roll 5 dice. Re-roll up to 2 times.");
        System.out.println("  Score is based on the final pattern.\n");

        // roll initial dice
        int[] dice = rollDice(5);
        printDice(dice);

        // up to 2 re-rolls
        for (int attempt = 1; attempt <= 2; attempt++)
        {
            System.out.println();
            System.out.println("  Re-roll attempt " + attempt + "/2. Enter dice positions to keep");
            System.out.println("  (e.g. 1 3 5), or ENTER to re-roll all, or 'done' to stop: ");

            String line = in.nextLine().trim();

            if (line.equalsIgnoreCase("done"))
            {
                // stop early
                break;
            }

            boolean[] keep = new boolean[5];   // default false = re-roll everything

            if (line.length() > 0)
            {
                // parse positions
                String[] parts = line.split("\\s+");
                for (int i = 0; i < parts.length; i++)
                {
                    try {
                        int pos = Integer.parseInt(parts[i]) - 1;
                        if (pos >= 0 && pos < 5) {
                            keep[pos] = true;
                        }
                    } catch (Exception ignored) {
                        // ignore bad tokens
                    }
                }
            }

            // re-roll dice that aren't kept
            for (int i = 0; i < dice.length; i++)
            {
                if (!keep[i]) {
                    dice[i] = rng.nextInt(6) + 1;
                }
            }

            printDice(dice);
        }

        // final output
        System.out.print("\n  Final dice: [");
        for (int i = 0; i < dice.length; i++)
        {
            System.out.print(dice[i]);
            if (i < dice.length - 1) System.out.print(", ");
        }
        System.out.println("]");

        String pattern = getPatternName(dice);
        int score = calculateScore(dice);

        System.out.println("  Pattern: " + pattern + " => " + score + " pts\n");

        return score;
    }


    // roll N dice
    private int[] rollDice(int n)
    {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++)
        {
            arr[i] = rng.nextInt(6) + 1;
        }
        return arr;
    }


    // print dice with positions
    private void printDice(int[] dice)
    {
        System.out.println();
        System.out.print("  Dice: ");
        for (int i = 0; i < dice.length; i++)
        {
            System.out.print("[" + (i + 1) + "]=" + dice[i] + "  ");
        }
        System.out.println();
    }


    // calculate score based on patterns
    private int calculateScore(int[] dice)
    {
        int[] sorted = dice.clone();
        Arrays.sort(sorted);

        // frequency map
        Map<Integer, Integer> freq = new HashMap<>();
        for (int i = 0; i < sorted.length; i++)
        {
            int v = sorted[i];
            Integer c = freq.get(v);
            if (c == null) c = 0;
            freq.put(v, c + 1);
        }

        // get counts sorted descending
        List<Integer> counts = new ArrayList<>(freq.values());
        counts.sort(Collections.reverseOrder());

        // pattern checks (in order)
        if (counts.get(0) == 5) return 50;   // Five of a Kind
        if (counts.get(0) == 4) return 40;   // Four of a Kind

        if (counts.get(0) == 3 && counts.size() > 1 && counts.get(1) == 2)
        {
            return 35;   // Full House
        }

        if (isStraight(sorted)) return 30;

        if (counts.get(0) == 3) return 25;

        int pairCount = 0;
        for (int c : counts)
        {
            if (c == 2) pairCount++;
        }

        if (pairCount == 2) return 20;
        if (counts.get(0) == 2) return 10;

        return 0;
    }


    // get pattern name (same logic as score)
    private String getPatternName(int[] dice)
    {
        int[] sorted = dice.clone();
        Arrays.sort(sorted);

        Map<Integer, Integer> freq = new HashMap<>();
        for (int v : sorted)
        {
            freq.put(v, freq.getOrDefault(v, 0) + 1);
        }

        List<Integer> counts = new ArrayList<>(freq.values());
        counts.sort(Collections.reverseOrder());

        if (counts.get(0) == 5) return "Five of a Kind";
        if (counts.get(0) == 4) return "Four of a Kind";
        if (counts.get(0) == 3 && counts.size() > 1 && counts.get(1) == 2) return "Full House";
        if (isStraight(sorted)) return "Straight";
        if (counts.get(0) == 3) return "Three of a Kind";

        int pairCount = 0;
        for (int c : counts) if (c == 2) pairCount++;

        if (pairCount == 2) return "Two Pairs";
        if (counts.get(0) == 2) return "One Pair";

        return "No Match";
    }


    // check straight (only 1-5 or 2-6)
    private boolean isStraight(int[] sorted)
    {
        if (sorted[0] != 1 && sorted[0] != 2)
        {
            return false;
        }

        for (int i = 1; i < sorted.length; i++)
        {
            if (sorted[i] != sorted[i - 1] + 1)
            {
                return false;
            }
        }

        return true;
    }
}
    
