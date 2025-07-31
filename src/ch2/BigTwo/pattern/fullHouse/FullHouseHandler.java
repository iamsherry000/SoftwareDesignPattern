package BigTwo.pattern.fullHouse;

import BigTwo.model.Card;
import BigTwo.pattern.CardPattern;
import BigTwo.pattern.PatternHandler;
import BigTwo.pattern.CardPatternUtil;
import java.util.List;

public class FullHouseHandler implements PatternHandler{
    private PatternHandler next;

    public void setNext(PatternHandler next) {
        this.next = next;
    }

    public CardPattern recognize(List<Card> cards) {
        if (CardPatternUtil.isFullHouse(cards)) {
            return new FullHouseCardPattern(cards); // 包裝起來
        } else if (next != null) {
            return next.recognize(cards);
        } else {
            return null;
        }
    }
}
