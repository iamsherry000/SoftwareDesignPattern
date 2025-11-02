package BigTwo.model;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<Card> cards = new ArrayList<>();

    public Deck() {
//        for(int suit = 0; suit < 4; suit++) {
//            for (int rank = 0; rank < 13; rank++) {
//                cards.add(new Card(suit, rank));
//            }
//        }
    }

//    private List<Card> cards;
//
//    public Deck(List<Card> cards) {
//        this.cards = cards;
//    }

    public void shuffle() {
        java.util.Collections.shuffle(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public Card draw() {
        if (isEmpty()) {
            throw new IllegalStateException("Deck is empty");
        }
        return cards.remove(cards.size() - 1);
    }

    //Test
    public Card getCard(int index) {
        if (index < 0 || index >= cards.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + cards.size());
        }
        return cards.get(index);
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards); // 返回副本以防止外部修改
    }

    public int getSize() {
        return cards.size();
    }

    public void add(Card newCard) {
        cards.add(newCard);
    }
}
