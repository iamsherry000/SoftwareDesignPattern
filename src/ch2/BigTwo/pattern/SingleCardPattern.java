package BigTwo.pattern;

import BigTwo.model.Card;
import BigTwo.pattern.CompareStrategy;
import BigTwo.pattern.SingleCompareStrategy;
import java.util.List;

public class SingleCardPattern implements CardPattern{
    private List<Card> cards;
    private CompareStrategy strategy;

    public SingleCardPattern(List<Card> cards) {
        this.cards = cards;
        this.strategy = new SingleCompareStrategy(); // 預設比較邏輯
    }

    public boolean isGreaterThan(CardPattern other) {
        return strategy.isGreater(this, other);
    }

    public List<Card> getCards() {
        return cards;
    }
}
