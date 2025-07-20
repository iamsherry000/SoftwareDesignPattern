package BigTwo.pattern;

import BigTwo.model.Card;
import java.util.List;

public class PairHandler implements PatternHandler {
    private PatternHandler next;

    public void setNext(PatternHandler next) {
        this.next = next;
    }

    public CardPattern recognize(List<Card> cards) {
        if (cards.size() == 2 && cards.get(0).getRank() == cards.get(1).getRank()) {
            return new PairCardPattern(cards); // 包裝起來
        } else if (next != null) {
            return next.recognize(cards);
        } else {
            return null;
        }
    }
}
