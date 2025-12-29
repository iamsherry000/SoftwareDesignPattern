package ch2_basicBV.BigTwo.V3.patterns;

import java.util.List;
import ch2_basicBV.BigTwo.V3.card.Card;

public class FullHouseHandler extends CardPatternHandler {
    public FullHouseHandler(CardPatternHandler next) {
        super(next);
    }

    public static class FullHouse extends CardPattern {
        private final List<Card> triple;
        private final List<Card> pair;
        
        protected FullHouse(List<Card> cards) {
            super("FullHouse", cards);
            this.triple = cards.subList(0, 3);
            this.pair = cards.subList(3, 5);
        }
    }
}
