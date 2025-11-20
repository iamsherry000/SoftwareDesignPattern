package BigTwo.pattern.fullHouse;

import BigTwo.model.Card;
import BigTwo.pattern.CardPattern;
import BigTwo.pattern.CompareStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullHouseCardPattern implements CardPattern{
    private final List<Card> cards;
    private final CompareStrategy compareStrategy;
    //more than other CardPattern
    private final int tripleRank; // use for comparison

    public FullHouseCardPattern(List<Card> cards) {
        this.cards = cards;
        this.compareStrategy = new FullHouseCompareStrategy();
        this.tripleRank = calculateTripleRank(cards);
    }

    private int calculateTripleRank(List<Card> cards) {
        Map<Integer, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }
        for (Map.Entry<Integer, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() == 3) { // 找到三張相同的牌
                return entry.getKey(); // 返回這張牌的數字
            }
        }
        return -1; // Default value, should not happen in a valid Full House
    }

    public int getTripleRank() {
        return tripleRank;
    }

    @Override
    public boolean isGreaterThan(CardPattern other) {
        return compareStrategy.isGreater(this, other);
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }
}
