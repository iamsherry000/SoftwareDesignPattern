package game.uno;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Deck extends game.base.Deck<Card> {
    public static Deck standardUnoDeck() {
        Deck deck = new Deck();
        for (int num = 0; num <= 9; num++) {
            for (Card.Color color : Card.Color.values()) {
                deck.push(new Card(num, color));
            }
        }
        return deck;
    }
}
