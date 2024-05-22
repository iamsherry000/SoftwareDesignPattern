package game.uno;

import java.util.List;

import static java.util.Arrays.asList;
import static game.uno.Deck.standardUnoDeck;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Main {
    public static void main(String[] args) {
        List<Player> players = asList(new AI(), new AI(), new AI(), new AI());
        Uno uno = new Uno(standardUnoDeck(), players);
        uno.start();
    }
}
