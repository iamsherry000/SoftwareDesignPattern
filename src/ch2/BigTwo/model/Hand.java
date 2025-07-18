package BigTwo.model;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> cards = new ArrayList<>();

    public void addCard(Card card) {
        cards.add(card);
    }

    // Check if the hand contains the club3
    public boolean contains(Card card) {
        return cards.contains(card);
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
