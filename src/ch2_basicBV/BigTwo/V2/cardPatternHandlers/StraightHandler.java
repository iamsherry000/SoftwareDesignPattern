package cardPatternHandlers;

import cardPatterns.CardPattern;
import cardPatterns.Straight;
import card.Card;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 順子牌型 Handler
 * 判斷條件：剛好 5 張牌，且點數連續
 */
public class StraightHandler extends CardPatternHandler {

    public StraightHandler(CardPatternHandler next) {
        super(next);
    }

    @Override
    protected CardPattern doHandle(List<Card> cards) {
        if (cards.size() != 5) {
            return null;
        }

        // 複製一份並按點數排序
        List<Card> sortedCards = new ArrayList<>(cards);
        sortedCards.sort(Comparator.comparingInt(c -> c.getRank().getValue()));

        // 檢查是否連續
        for (int i = 0; i < sortedCards.size() - 1; i++) {
            int currentValue = sortedCards.get(i).getRank().getValue();
            int nextValue = sortedCards.get(i + 1).getRank().getValue();
            if (currentValue + 1 != nextValue) {
                return null;
            }
        }

        return new Straight(cards);
    }
}


