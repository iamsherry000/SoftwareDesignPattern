package ch2_basicBV.BigTwo.V3.patterns;

import java.util.List;
import ch2_basicBV.BigTwo.V3.card.Card;

public abstract class CardPattern {
    protected String name; 
    protected List<Card> cards;

    public CardPattern(String name, List<Card> cards) {
        this.name = name;
        this.cards = cards;
    }

    public boolean isSamePattern(CardPattern c){
        return getName().equals(c.getName()) && getClass() == c.getClass();
    }

    public String getName() {
        return name;
    }

    public List<Card> getCards() {
        return cards;
    }
}
