package BigTwo.pattern.fullHouse;

import BigTwo.pattern.CompareStrategy;
import BigTwo.pattern.CardPattern;
import BigTwo.pattern.fullHouse.FullHouseCardPattern;

public class FullHouseCompareStrategy implements CompareStrategy {
    @Override
    public boolean isGreater(CardPattern pattern1, CardPattern pattern2) {
        // Make sure both patterns are FullHouseCardPattern
        if (!(pattern1 instanceof FullHouseCardPattern) || !(pattern2 instanceof FullHouseCardPattern)) {
            return false;
        }

        // How to compare Full House?  以三張數字相同的牌中，數字最大的牌作為比較基準 => FullHouseCardPattern tripleRank

        FullHouseCardPattern fh1 = (FullHouseCardPattern) pattern1;
        FullHouseCardPattern fh2 = (FullHouseCardPattern) pattern2;

        return fh1.getTripleRank() > fh2.getTripleRank();
    }
}
