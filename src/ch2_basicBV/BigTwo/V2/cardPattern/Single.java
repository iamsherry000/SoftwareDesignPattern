package cardPattern;

import card.Card;

public class Single implements CardPattern {
    private final Card card;
    
    public Single(Card card) {
        this.card = card;
    }

    @Override
    public boolean isGreaterThan(CardPattern other) {
        // if(this.card.getRank() != other.getRank()) {
        //     return this.card.getRank().ordinal() > other.card.getRank().ordinal();
        // }
        // else {
        //     return this.card.getSuit().ordinal() > other.card.getSuit().ordinal();
        // }
        return false;
    }
}
