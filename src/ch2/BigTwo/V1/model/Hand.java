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

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public List<Card> getOrderedHand() {
        cards.sort((c1, c2) -> {
            if (c1.getRank() != c2.getRank()) {
                return Integer.compare(c1.getRank(), c2.getRank());
            } else {
                return Integer.compare(c1.getSuit(), c2.getSuit());
            }
        });
        return cards;
    }

    public Card getCard(int index) {
        if (index >= 0 && index < cards.size()) {
            return cards.get(index);
        } else {
            throw new IndexOutOfBoundsException("Out of index：" + index);
        }
    }


    public void removeCards(List<Card> playedCards) {
        for (Card card : playedCards) {
            if (!cards.remove(card)) {
                throw new IllegalArgumentException("Card not found in hand: " + card);
            }
        }
    }
}
