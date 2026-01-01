package ch2_basicBV.BigTwo.V3.patterns;

import java.util.Arrays;
import java.util.List;
import ch2_basicBV.BigTwo.V3.card.Card;

public class SingleHandler extends CardPatternHandler {
    public SingleHandler(CardPatternHandler next) {
        super(next);
    }

    @Override
    protected CardPattern tryHandle(List<Card> cards) {
        if (cards.size() == 1) {
            return new Single(cards.get(0));
        }
        return null;
    }

    public static class Single extends CardPattern {
        protected Card card;
        public Single(Card card) {
            super("Single", Arrays.asList(card));
            this.card = card;
        }
    }
}
