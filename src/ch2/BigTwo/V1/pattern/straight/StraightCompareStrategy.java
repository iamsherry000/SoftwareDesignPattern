package BigTwo.pattern.straight;

import BigTwo.pattern.CardPattern;
import BigTwo.pattern.CompareStrategy;
import BigTwo.model.Card;
import java.util.List;

public class StraightCompareStrategy implements CompareStrategy {
    @Override
    public boolean isGreater(CardPattern pattern1, CardPattern pattern2) {
        // 確保兩邊都是 StraightCardPattern
        if (!(pattern1 instanceof StraightCardPattern) || !(pattern2 instanceof StraightCardPattern)) {
            return false;
        }

        // 取得兩個牌組的 last card
        List<Card> cards1 = pattern1.getCards();
        List<Card> cards2 = pattern2.getCards();
        Card a = cards1.get(cards1.size() - 1);  // 最後一張
        Card b = cards2.get(cards2.size() - 1);  // 最後一張

        // 比較兩張牌的等級
        if (a.getRank() != b.getRank()) {
            return a.getRank() > b.getRank();
        } else { // 等級相同，則比較花色
            return a.getSuit() > b.getSuit();
        }
    }
}
