package src.tw.SherryTseng.CardGames;

import java.util.List;
import java.util.Scanner;

public class HumanPlayer extends Player {
    private Scanner scanner;

    public HumanPlayer(String name) {
        super(name); // or super(null)?
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void nameHimself() {
        System.out.println("Please enter your name: ");
        this.name = scanner.nextLine();
    }

    @Override
    public Card playCard() {
        // Display player's hand
        System.out.println(name + "'s hand: " + hand);

        // Let the player choose a card from their hand
        System.out.println("Choose a card to play (enter the index of the card): ");
        int index = scanner.nextInt();
        scanner.nextLine(); // consume newline character

        // Remove and return the chosen card from the hand
        return hand.removeCard(index);
    }

}
