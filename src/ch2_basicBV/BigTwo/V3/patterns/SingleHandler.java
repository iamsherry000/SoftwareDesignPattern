package ch2_basicBV.BigTwo.V3.patterns;

import java.util.Arrays;
import ch2_basicBV.BigTwo.V3.card.Card;

public class SingleHandler extends CardPatternHandler {
    public SingleHandler(CardPatternHandler next) {
        super(next);
    }

    public static class Single extends CardPattern {
        protected Card card;
        public Single(Card card) {
            super("Single", Arrays.asList(card));
            this.card = card;
        }
    }
}
