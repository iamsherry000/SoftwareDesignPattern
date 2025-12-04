import java.util.List;
import java.util.ArrayList;

import card.Card;

public class Hand {
    private final List<Card> hand;

    public Hand() {
        this.hand = new ArrayList<>();
    }

    public void add(Card card) {
        hand.add(card);
    }

    public void remove(Card card) {
        hand.remove(card);
    }
    
    public boolean isEmpty() {
        return hand.isEmpty();
    }

    public boolean hasCard(Card card) {
        return hand.contains(card);
    }

    public String toString() {
        return hand.toString();
    }
    
}
