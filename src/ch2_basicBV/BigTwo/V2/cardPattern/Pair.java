package cardPattern;

import card.Card;

public class Pair {
    private final Card card1;
    private final Card card2;

    public Pair(Card card1, Card card2) {
        this.card1 = card1;
        this.card2 = card2;
    }

    private Card getHigherCard() {
        if(this.card1.getRank().ordinal() > this.card2.getRank().ordinal()) {
            return this.card1;
        }
        else {
            return this.card2;
        }
    }
    
    public boolean isGreaterThan(Pair other) {
        Card card = getHigherCard();
        
        if(card.getRank() != card.getRank()) {
            return card.getRank().ordinal() > card.getRank().ordinal();
        }
        else {
            return card.getSuit().ordinal() > card.getSuit().ordinal();
        }
    }
}
