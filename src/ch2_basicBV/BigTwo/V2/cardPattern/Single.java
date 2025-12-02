package cardPattern;

import card.Card;
import java.util.List;

public class Single extends CardPattern {
    
    public Single(List<Card> cards) {
        super(cards);  // 呼叫父類別的建構子
        
        // 驗證：單張牌型 = 1 張牌
        if (cards.size() != 1) {
            throw new IllegalArgumentException(
                "Single must have exactly 1 card, but got " + cards.size()
            );
        }
    }
}
