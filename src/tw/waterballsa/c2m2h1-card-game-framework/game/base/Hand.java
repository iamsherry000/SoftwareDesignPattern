package game.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Hand<Card> implements Iterable<Card> {
    private final List<Card> cards = new ArrayList<>();

    public void addCard(Card card) {
        cards.add(card);
    }

    public Card get(int index) {
        return cards.get(index);
    }

    public Card play(int index) {
        return cards.remove(index);
    }

    public int size() {
        return cards.size();
    }

    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public Stream<Card> stream() {
        return cards.stream();
    }

    public List<Card> asList() {
        return cards;
    }
}
