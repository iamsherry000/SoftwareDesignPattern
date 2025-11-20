package TemplateMethod.practice;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Deck {
    protected List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public void shuffle() {
        // 洗牌邏輯
        Collections.shuffle(cards);
    }

    public Card draw() {
        if(cards.isEmpty()) {
            throw new IllegalStateException("No cards left in the deck.");
        }

        return cards.remove(0); // 從牌堆頂部抽一張牌
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    // Uno only
    public void reset(List<Card> recycledCards) {
        cards.clear();
        cards.addAll(recycledCards);
        shuffle();
    }
}
