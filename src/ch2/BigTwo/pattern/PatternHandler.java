package BigTwo.pattern;

import BigTwo.model.Card;
import java.util.List;
import BigTwo.pattern.CardPattern;

public interface PatternHandler {
    void setNext(PatternHandler next);
    CardPattern recognize(List<Card> cards);
}
