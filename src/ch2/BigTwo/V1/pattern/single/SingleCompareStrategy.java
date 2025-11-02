package BigTwo.pattern.single;

import BigTwo.model.Card;
import BigTwo.pattern.CardPattern;
import BigTwo.pattern.CompareStrategy;

public class SingleCompareStrategy implements CompareStrategy {
    @Override
    public boolean isGreater(CardPattern pattern1, CardPattern pattern2) {
        // 確保兩邊都是 SingleCardPattern
        if (!(pattern1 instanceof SingleCardPattern) || !(pattern2 instanceof SingleCardPattern)) {
            return false;
        }
        Card a = pattern1.getCards().get(0);
        Card b = pattern2.getCards().get(0);

        // compare the two cards
        if (a.getRank() != b.getRank()) {
            return a.getRank() > b.getRank();
        }
        else{ // rank is the same, compare suit
            return a.getSuit() > b.getSuit();
        }
    }
}
