package BigTwo.pattern;

import BigTwo.model.Card;
import java.util.List;

/*
* CardPattern should follow SRP.
* I was thinking if I should make a "AbstractCardPattern", but think about the flexibility, I didn't.
 */

public interface CardPattern {
    boolean isGreaterThan(CardPattern other);
    List<Card> getCards();
}
