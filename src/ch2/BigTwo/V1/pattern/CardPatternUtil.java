package BigTwo.pattern;

import BigTwo.model.*;
import java.util.List;

public class CardPatternUtil {
    public static boolean isPair(List<Card> cards) {
        return cards.size() == 2 && cards.get(0).getRank() == cards.get(1).getRank();
    }

    public static boolean isStraight(List<Card> cards) {
        if (cards.size() != 5) return false;

        // Sort cards using the custom rank order
        cards.sort((c1, c2) -> getCustomRank(c1.getRank()) - getCustomRank(c2.getRank()));

        // Check if the cards form a valid straight
        for (int i = 1; i < cards.size(); i++) {
            int currentRank = getCustomRank(cards.get(i - 1).getRank());
            int nextRank = getCustomRank(cards.get(i).getRank());

            // Handle wrap-around case (e.g., K-A-2-3-4)
            if (currentRank == 12 && nextRank == 0) continue;

            if (nextRank - currentRank != 1) return false;
        }
        return true;
    }

    private static int getCustomRank(int rank) {
        // Custom rank order: 3 < 4 < 5 < ... < 10 < J < Q < K < A < 2
        int[] rankOrder = {3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 1, 2};
        int[] customOrder = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

        if (rank < 0 || rank >= customOrder.length) {
            throw new IllegalArgumentException("Invalid card rank: " + rank);
        }

        // 這邊直接回傳 custom sort index
        return (rank + 11) % 13;  // 將 3~A~2 排序成 0~12
    }

    public static boolean isFullHouse(List<Card> cards) {
        if (cards.size() != 5) return false;
        int[] rankCount = new int[13]; // 0-12 for 2-A
        for (Card card : cards) {
            rankCount[card.getRank()]++;
        }
        boolean hasThree = false, hasTwo = false;
        for (int count : rankCount) {
            if (count == 3) hasThree = true;
            if (count == 2) hasTwo = true;
        }
        return hasThree && hasTwo;
    }
}
