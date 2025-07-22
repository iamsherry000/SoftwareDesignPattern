package BigTwo.pattern.pair;

import BigTwo.model.Card;
import BigTwo.pattern.CardPattern;
import BigTwo.pattern.CompareStrategy;

import java.util.List;

public class PairCardPattern implements CardPattern {
    private final List<Card> cards;
    private final CompareStrategy compareStrategy;

    public PairCardPattern(List<Card> cards) {
        this.cards = cards;
        this.compareStrategy = new PairCompareStrategy();
    }

    @Override
    public boolean isGreaterThan(CardPattern other) {
        return compareStrategy.isGreater(this, other);
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }
}
