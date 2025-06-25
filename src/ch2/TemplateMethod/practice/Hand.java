package TemplateMethod.practice;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private final List<Card> cards = new ArrayList<>();
    private final int maxSize;

    public Hand(int maxSize) {
        this.maxSize = maxSize;
    }

    public void add(Card card) {
        if(cards.size() >= maxSize) {
            throw new IllegalStateException("Hand is full.");
        }
        cards.add(card);
    }

    public Card play(int index) {
        if (index < 0 || index >= cards.size()) {
            throw new IndexOutOfBoundsException("Invalid card index");
        }
        return cards.remove(index);
    }

    public int size() {
        return cards.size();
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards); // 傳回複本以避免外部修改
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public boolean hasPlayable(Card topCard) {
        return cards.stream().anyMatch(card -> card.canPlayOn(topCard));
    }

    public List<Card> getPlayableCards(Card topCard) {
        List<Card> result = new ArrayList<>();
        for (Card card : cards) {
            if (card.canPlayOn(topCard)) {
                result.add(card);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
