package cardPatternHandlers;

import cardPatterns.CardPattern;
import cardPatterns.Single;
import card.Card;
import java.util.List;

/**
 * 單張牌型 Handler
 * 判斷條件：剛好 1 張牌
 */
public class SingleHandler extends CardPatternHandler {

    public SingleHandler(CardPatternHandler next) {
        super(next);
    }

    @Override
    protected CardPattern doHandle(List<Card> cards) {
        if (cards.size() == 1) {
            return new Single(cards);
        }
        return null;
    }
}
