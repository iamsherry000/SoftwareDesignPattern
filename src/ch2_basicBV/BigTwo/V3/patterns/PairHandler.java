package ch2_basicBV.BigTwo.V3.patterns;

import java.util.List;
import ch2_basicBV.BigTwo.V3.card.Card;

public class PairHandler extends CardPatternHandler {
    public PairHandler(CardPatternHandler next) {
        super();
    }

    private boolean isPair(List<Card> cards) {
        return cards.size() == 2 && cards.get(0).getRank() == cards.get(1).getRank();
    }

    public static class Pair extends CardPattern {
        
        protected Pair(List<Card> cards) {
            super("Pair", cards);
        }
    }
}
