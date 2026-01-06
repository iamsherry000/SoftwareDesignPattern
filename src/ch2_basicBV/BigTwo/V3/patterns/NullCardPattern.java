package ch2_basicBV.BigTwo.V3.patterns;

import java.util.List;
import ch2_basicBV.BigTwo.V3.card.Card;

public class NullCardPattern extends CardPatternHandler {
    public final static NullCardPattern INSTANCE = new NullCardPattern();
    
    protected NullCardPattern() {
        super();
    }

    @Override
    public CardPattern handle(List<Card> cards) {
        return null; // NullCardPattern 是終點，直接返回 null
    }

    @Override
    protected CardPattern tryHandle(List<Card> cards) {
        return null; // NullCardPattern 總是返回 null
    }
}
