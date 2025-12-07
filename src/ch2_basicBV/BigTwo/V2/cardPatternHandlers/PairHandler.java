package cardPatternHandlers;

import cardPatterns.CardPattern;
import cardPatterns.Pair;
import card.Card;
import java.util.List;

/**
 * 對子牌型 Handler
 * 判斷條件：剛好 2 張牌，且點數相同
 */
public class PairHandler extends CardPatternHandler {

    public PairHandler(CardPatternHandler next) {
        super(next);
    }

    @Override
    protected CardPattern doHandle(List<Card> cards) {
        if (cards.size() != 2) {
            return null;
        }
        
        Card card1 = cards.get(0);
        Card card2 = cards.get(1);
        
        // 檢查兩張牌的點數是否相同
        if (card1.getRank().getValue() == card2.getRank().getValue()) {
            return new Pair(cards);
        }
        return null;
    }
}


