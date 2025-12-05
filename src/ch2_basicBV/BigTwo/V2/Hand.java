import card.Card;
import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> hand;

    public Hand() {
        this.hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }
    
    // public boolean isEmpty() {
    //     return hand.isEmpty();
    // }

    public boolean hasCard(Card card) {
        return hand.contains(card);
    }

    public String toString() {
        return hand.toString();
    }
}
