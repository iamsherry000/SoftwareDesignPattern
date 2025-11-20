package BigTwo.pattern.straight;

import BigTwo.model.Card;
import BigTwo.pattern.CardPattern;
import BigTwo.pattern.PatternHandler;
import BigTwo.pattern.CardPatternUtil;
import java.util.List;

public class StraightHandler implements PatternHandler {
    private PatternHandler next;

    public void setNext(PatternHandler next) {
        this.next = next;
    }

    public CardPattern recognize(List<Card> cards) {
        if (CardPatternUtil.isStraight(cards)) {
            return new StraightCardPattern(cards); // 包裝起來
        } else if (next != null) {
            return next.recognize(cards);
        } else {
            return null;
        }
    }
}
