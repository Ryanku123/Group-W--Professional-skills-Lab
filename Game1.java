import java.util.*;
public class Game1_DicePatterns implements Game{

    private static final String NAME = "Dice Patterns Challenge";
    private final Scanner sc = new Scanner(System.in);
    private final DiceRoller roller = new DiceRoller();
    private final DicePrinter printer = new DicePrinter();
    private final DiceScorer scorer = new DiceScorer();

    @Override
    public String getName(){
        return NAME;
    }

    @Override
    public int play(User user){

        System.out.println("===============================");
        System.out.println("GAME 1: Dice Patterns Challenge");
        System.out.println("===============================");
        System.out.println("Roll 5 dice. Re-roll up to 2 times.");
        System.out.println("Score is based on the final pattern.");
        System.out.println();

        int[] dice = roller.rollAll(5);
        printer.printDice(dice);

        for (int reroll = 1; reroll <= 2; reroll++){
            System.out.println();
            System.out.println("Re-roll attempt " + reroll +"/2. Enter dice positions to keep");
            System.out.println("(e.g. 1 3 5), or ENTER to re-roll all, or 'done' to stop:");
// read one line of input 
            String input = sc.nextLine().trim(); // remove leading/trailing whitespace

            if (input.equalsIgnoreCase("done")) break;

            boolean[] keep = new boolean[5];

            if(!input.isEmpty()){
                for(String tok : input.split("\\s+")){
                    try {
                        int pos = Integer.parseInt(tok)-1;
                        if (pos>=0 && pos<5) keep[pos]=true;
                    } catch (Exception ignored){}
                }
            }

            roller.reroll(dice,keep);
            printer.printDice(dice);
        }

        System.out.println();
        System.out.println("Final dice: "+Arrays.toString(dice));

        String pattern = scorer.getPatternName(dice);
        int score = scorer.calculateScore(dice);

        System.out.println("Pattern: "+pattern+" => "+score+" pts");

        return score;
    }
}
