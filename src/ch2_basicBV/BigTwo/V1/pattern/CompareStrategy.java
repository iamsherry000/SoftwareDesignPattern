package BigTwo.pattern;

import BigTwo.model.Card;

public interface CompareStrategy {
    boolean isGreater(CardPattern pattern1, CardPattern pattern2);
}
