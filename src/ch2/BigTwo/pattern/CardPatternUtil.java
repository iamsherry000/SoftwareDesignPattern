package BigTwo.pattern;

import BigTwo.model.*;
import java.util.List;

public class CardPatternUtil {
    public static boolean isPair(List<Card> cards) {
        return cards.size() == 2 && cards.get(0).getRank() == cards.get(1).getRank();
    }

    public static boolean isStraight(List<Card> cards) {
        if (cards.size() != 5) return false;
        cards.sort((c1, c2) -> c1.getRank() - c2.getRank());
        for (int i = 1; i < cards.size(); i++) {
            if (cards.get(i).getRank() != cards.get(i - 1).getRank() + 1) {
                return false;
            }
        }
        return true;
    }
}
