package BigTwo.pattern.pair;

import BigTwo.model.Card;
import BigTwo.pattern.CardPattern;
import BigTwo.pattern.CompareStrategy;

public class PairCompareStrategy implements CompareStrategy {
    @Override
    public boolean isGreater(CardPattern pattern1, CardPattern pattern2) {
        // 確保兩邊都是 PairCardPattern
        if (!(pattern1 instanceof PairCardPattern) || !(pattern2 instanceof PairCardPattern)) {
            return false;
        }
        // 取得兩個牌組的第一張牌
        Card a = pattern1.getCards().get(1);
        Card b = pattern2.getCards().get(1);

        // 比較兩張牌的等級
        if (a.getRank() != b.getRank()) {
            return a.getRank() > b.getRank();
        } else { // 等級相同，則比較花色
            return a.getSuit() > b.getSuit();
        }
    }
}
