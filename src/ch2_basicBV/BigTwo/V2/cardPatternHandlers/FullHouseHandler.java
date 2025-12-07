package cardPatternHandlers;

import cardPatterns.CardPattern;
import cardPatterns.FullHouse;
import card.Card;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 葫蘆牌型 Handler
 * 判斷條件：剛好 5 張牌，其中 3 張同點數 + 2 張同點數
 */
public class FullHouseHandler extends CardPatternHandler {

    public FullHouseHandler(CardPatternHandler next) {
        super(next);
    }

    @Override
    protected CardPattern doHandle(List<Card> cards) {
        if (cards.size() != 5) {
            return null;
        }

        // 統計每個點數出現的次數
        Map<Integer, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            int rankValue = card.getRank().getValue();
            rankCount.put(rankValue, rankCount.getOrDefault(rankValue, 0) + 1);
        }

        // 葫蘆必須剛好有 2 種不同點數，且一種出現 3 次、另一種出現 2 次
        if (rankCount.size() != 2) {
            return null;
        }

        boolean hasThree = false;
        boolean hasTwo = false;
        for (int count : rankCount.values()) {
            if (count == 3) hasThree = true;
            if (count == 2) hasTwo = true;
        }

        if (hasThree && hasTwo) {
            return new FullHouse(cards);
        }
        return null;
    }
}


