package abstractClass;

import java.util.Scanner;

public class HumanPlayer extends Player{
    private final static Scanner input = new Scanner(System.in);

    public HumanPlayer(int number) {
        super(number);
    }

    @Override
    public Decision decide() {
        System.out.println("1. Rock 2. Paper 3. Scissors : ");
        int num = input.nextInt();
        switch(num) {
            case 1: return Decision.ROCK;
            case 2: return Decision.PAPER;
            case 3: return Decision.SCISSORS;
            default:
                System.out.println("Invalid input.");
                return decide();
        }
    }
}
