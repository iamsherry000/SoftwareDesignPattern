package src.tw.SherryTseng.CardGames;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> cards;

    // constructor
    public Hand() {
        this.cards = new ArrayList<>();
    }
    
    public void addCard(Card card) {
        cards.add(card);
    }

    public Card removeCard(int index) {
        if (index < 0 || index >= cards.size()) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return cards.remove(index);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

    public List<Card> getCard() {
        return cards;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card.getSuit()).append(" ").append(card.getRank()).append(", ");
        }
        return sb.toString();
    }
    
}
