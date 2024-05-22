package game.showdown;

import java.util.List;

import static java.util.Arrays.asList;
import static game.showdown.Deck.standard52Cards;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Main {
    public static void main(String[] args) {
        List<Player> players = asList(new AI(), new AI(), new AI(), new AI());
        Showdown showdown = new Showdown(standard52Cards(), players);
        players.forEach(p -> p.setShowdown(showdown));
        showdown.start();
    }
}
