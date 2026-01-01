package ch2_basicBV.BigTwo.V3.patterns;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import ch2_basicBV.BigTwo.V3.card.Card;
import ch2_basicBV.BigTwo.V3.card.Rank;

public class FullHouseHandler extends CardPatternHandler {
    public FullHouseHandler(CardPatternHandler next) {
        super(next);
    }

    @Override
    protected CardPattern tryHandle(List<Card> cards) {
        if (isFullHouse(cards)) {
            return new FullHouse(cards);
        }
        return null;
    }

    private boolean isFullHouse(List<Card> cards) {
        if (cards.size() != 5) return false;
        
        Map<Rank, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }
        
        // 葫蘆必須有一個三條和一個對子
        boolean hasTriple = false;
        boolean hasPair = false;
        
        for (int count : rankCount.values()) {
            if (count == 3) hasTriple = true;
            else if (count == 2) hasPair = true;
            else if (count != 2 && count != 3) return false; // 有其他數量的牌
        }
        
        return hasTriple && hasPair;
    }

    public static class FullHouse extends CardPattern {
        private final List<Card> triple;
        private final List<Card> pair;
        
        protected FullHouse(List<Card> cards) {
            super("FullHouse", cards);
            this.triple = cards.subList(0, 3);
            this.pair = cards.subList(3, 5);
        }
    }
}
