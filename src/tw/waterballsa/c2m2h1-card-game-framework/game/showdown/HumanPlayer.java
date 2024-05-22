package game.showdown;

import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.String.format;
import static game.utils.Utils.printf;
import static game.utils.Utils.println;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class HumanPlayer extends Player {
    private final static Scanner in = new Scanner(System.in);

    @Override
    public void nameSelf(int order) {
        printf("Input your name (P%d): ", order);
        String name = in.next();
        if (name.isEmpty()) {
            nameSelf(order);
        } else {
            setName(name);
        }
    }

    @Override
    protected Card takeTurn() {
        printCardSelections();
        try {
            int choice = in.nextInt();
            if (choice < 0 || choice >= getHand().size()) {
                return takeTurn();
            }
            return getHand().play(choice);
        } catch (InputMismatchException e) {
            return takeTurn();
        }
    }

    private void printCardSelections() {
        println("Select the card to play (y/n): ");
        StringBuilder numbers = new StringBuilder();
        StringBuilder cards = new StringBuilder();
        for (int i = 0; i < getHand().size(); i++) {
            String cardRepresentation = getHand().get(i).toString();
            numbers.append(format("%"+(-cardRepresentation.length())+"s", i)).append(" ");
            cards.append(cardRepresentation).append(" ");
        }

        println(numbers.toString().stripTrailing());
        println(cards.toString().stripTrailing());
    }
}
