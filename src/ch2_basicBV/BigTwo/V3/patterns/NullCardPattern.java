package ch2_basicBV.BigTwo.V3.patterns;

import java.util.List;
import ch2_basicBV.BigTwo.V3.card.Card;

public class NullCardPattern extends CardPatternHandler {
    public final static NullCardPattern INSTANCE = new NullCardPattern();
    
    protected NullCardPattern() {
        super();
    }

    @Override
    protected CardPattern tryHandle(List<Card> cards) {
        return null; // NullCardPattern 總是返回 null
    }
}
