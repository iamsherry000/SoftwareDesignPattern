import card.Card;
import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }
    
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

    public boolean hasCard(Card card) {
        return cards.contains(card);
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards); // 回傳副本以保護內部狀態
    }

    public String toString() {
        return cards.toString();
    }
}
