package ch2_basicBV.BigTwo.V3.card;

import java.util.Stack;
import java.util.Collections;

public class Deck {
    private final Stack<Card> cards;

    public Deck() {
        this.cards = new Stack<>();
        for(Suit suit : Suit.values()) {
            for(Rank rank : Rank.values()) {
                Card card = new Card(suit, rank);
                cards.push(card);
                // System.out.println("just added: " + card + ", size now = " + cards.size());
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Deck is empty");
        }
        return cards.pop();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public void add(Card card) {
        cards.push(card);
    }
}
