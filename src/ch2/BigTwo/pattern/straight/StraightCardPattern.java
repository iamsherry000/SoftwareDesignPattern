package BigTwo.pattern.straight;

import BigTwo.model.Card;
import BigTwo.pattern.CardPattern;
import BigTwo.pattern.CompareStrategy;

import java.util.List;

public class StraightCardPattern implements CardPattern{
    private final List<Card> cards;
    private final CompareStrategy compareStrategy;

    public StraightCardPattern(List<Card> cards) {
        this.cards = cards;
        this.compareStrategy = new StraightCompareStrategy();
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
