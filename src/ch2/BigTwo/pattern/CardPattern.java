package BigTwo.pattern;

import BigTwo.model.Card;
import java.util.List;

public interface CardPattern {
    boolean isGreaterThan(CardPattern other);
    List<Card> getCards();
}
