package game.base;

import java.util.Collections;
import java.util.Stack;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Deck<Card> {
    private final Stack<Card> cardStack = new Stack<>();

    public void push(Card card) {
        cardStack.push(card);
    }

    public Card draw() {
        return cardStack.pop();
    }

    public void shuffle() {
        Collections.shuffle(cardStack);
    }

    public int size() {
        return cardStack.size();
    }

    public boolean isEmpty() {
        return cardStack.isEmpty();
    }

    public void push(Deck<Card> deck) {
        cardStack.addAll(deck.cardStack);
    }

    public void clear() {
        cardStack.clear();
    }
}
