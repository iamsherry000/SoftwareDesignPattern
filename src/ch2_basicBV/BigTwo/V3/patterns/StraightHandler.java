package ch2_basicBV.BigTwo.V3.patterns;

import java.util.List;
import ch2_basicBV.BigTwo.V3.card.Card;

public class StraightHandler extends CardPatternHandler {
    public StraightHandler(CardPatternHandler next) {
        super(next);
    }

    
    public static class Straight extends CardPattern {
        private final Card root;
        
        protected Straight(List<Card> cards) {
            super("Straight", cards);
            this.root = (Card) cards.toArray()[0];
        }
    }
}
