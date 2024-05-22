package game.showdown;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Deck extends game.base.Deck<Card> {
    public static Deck standard52Cards() {
        Deck deck = new Deck();
        Card.Suit[] suits = Card.Suit.values();
        Card.Rank[] ranks = Card.Rank.values();
        for (Card.Suit suit : suits) {
            for (Card.Rank rank : ranks) {
                deck.push(new Card(suit, rank));
            }
        }
        return deck;
    }
}
