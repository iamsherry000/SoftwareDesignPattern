package ch2_basicBV.BigTwo.V3.card;

import java.util.List;
import java.util.ArrayList;

public class Hand {
    private List<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public void add(List<Card> cards) {
        for(Card card : cards) {
            this.cards.add(card);
        }
    }

    public void remove(List<Card> cards) {
        this.cards.removeAll(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public boolean contains(List<Card> cards) {
        return this.cards.containsAll(cards);
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    public int size() {
        return cards.size();
    }
    
    @Override
    public String toString() {
        if (cards.isEmpty()) {
            return "No cards";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(cards.get(i).toString());
        }
        return sb.toString();
    }
}
