package ch2_basicBV.BigTwo.V3.patterns;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import ch2_basicBV.BigTwo.V3.card.Card;
import ch2_basicBV.BigTwo.V3.card.Rank;

public class StraightHandler extends CardPatternHandler {
    public StraightHandler(CardPatternHandler next) {
        super(next);
    }

    @Override
    protected CardPattern tryHandle(List<Card> cards) {
        if (isStraight(cards)) {
            return new Straight(cards);
        }
        return null;
    }

    private boolean isStraight(List<Card> cards) {
        if (cards.size() != 5) return false;
        
        List<Card> sortedCards = new ArrayList<>(cards);
        sortedCards.sort((c1, c2) -> Integer.compare(getBigTwoRankValue(c1.getRank()), getBigTwoRankValue(c2.getRank())));
        
        // 檢查是否為連續數字
        for (int i = 1; i < sortedCards.size(); i++) {
            int prevValue = getBigTwoRankValue(sortedCards.get(i-1).getRank());
            int currValue = getBigTwoRankValue(sortedCards.get(i).getRank());
            
            // 處理循環順子的情況 (如 K-A-2-3-4)
            if (currValue - prevValue != 1 && !(prevValue == 14 && currValue == 15)) { // A to 2
                // 檢查是否為循環順子
                if (!isCircularStraight(sortedCards)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isCircularStraight(List<Card> sortedCards) {
        // 檢查特殊的循環順子情況，如 K-A-2-3-4
        // 這裡簡化處理，實際可能需要更複雜的邏輯
        return false; // 暫時不支援循環順子
    }

    private int getBigTwoRankValue(Rank rank) {
        switch (rank) {
            case THREE: return 3;
            case FOUR: return 4;
            case FIVE: return 5;
            case SIX: return 6;
            case SEVEN: return 7;
            case EIGHT: return 8;
            case NINE: return 9;
            case TEN: return 10;
            case JACK: return 11;
            case QUEEN: return 12;
            case KING: return 13;
            case ACE: return 14;
            case TWO: return 15;
            default: throw new IllegalArgumentException("Unknown rank: " + rank);
        }
    }
    
    public static class Straight extends CardPattern {
        private final Card root;
        
        protected Straight(List<Card> cards) {
            super("Straight", cards);
            this.root = (Card) cards.toArray()[0];
        }
    }
}
