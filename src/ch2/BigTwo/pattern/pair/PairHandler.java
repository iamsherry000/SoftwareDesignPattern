package BigTwo.pattern.pair;

import BigTwo.model.Card;
import BigTwo.pattern.CardPattern;
import BigTwo.pattern.PatternHandler;
import BigTwo.pattern.CardPatternUtil;

import java.util.List;

public class PairHandler implements PatternHandler {
    private PatternHandler next;

    public void setNext(PatternHandler next) {
        this.next = next;
    }

    public CardPattern recognize(List<Card> cards) {
        if (CardPatternUtil.isPair(cards)) {
            return new PairCardPattern(cards); // 包裝起來
        } else if (next != null) {
            return next.recognize(cards);
        } else {
            return null;
        }
    }
}
