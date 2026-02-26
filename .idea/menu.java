import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("C) Dice Pattern Challange");
        System.out.println("L) Dice Grid Puzzle");
        System.out.println("U) Dice Codebreaker");
        System.out.println("M) Blackjack With Dice");
        System.out.println("K) Score ");
        System.out.println("Q) Quit");

        String options = null;
        Scanner scan = new Scanner(System.in); // Capturing the input
        do {
            options = scan.nextLine();
            switch (options) {
                case "C":
                    // do what you want
                    break;
                case "L":
                    // do what you want
                    break;
                case "U":
                    // do what you want
                    break;
                case "M":
                    break;
                    // Add the rest of your cases
                case "K":
                    break;
                    // Add the rest of your cases
            }
        } while (!options.equals("Q")); // quitting the program
    }
}
