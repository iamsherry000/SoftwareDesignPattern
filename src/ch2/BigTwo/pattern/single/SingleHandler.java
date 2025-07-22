package BigTwo.pattern.single;

import BigTwo.model.Card;
import BigTwo.pattern.CardPattern;
import BigTwo.pattern.PatternHandler;

import java.util.List;

public class SingleHandler implements PatternHandler {
    private PatternHandler next;

    public void setNext(PatternHandler next) {
        this.next = next;
    }

    public CardPattern recognize(List<Card> cards) {
        if (cards.size() == 1) {
            return new SingleCardPattern(cards); // 包裝起來
        } else if (next != null) {
            return next.recognize(cards);
        } else {
            return null;
        }
    }
}
