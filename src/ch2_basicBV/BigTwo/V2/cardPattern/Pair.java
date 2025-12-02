package cardPattern;

import card.Card;
import java.util.List;

public class Pair extends CardPattern{
    public Pair(List<Card> cards) {
        super(cards);  // 呼叫父類別的建構子
        
        // 驗證：pair = 2 張牌
        if (cards.size() != 2) {
            throw new IllegalArgumentException(
                "Pair must have exactly 2 cards, but got " + cards.size()
            );
        }
    }

    // private Card getBigestCard() {
    //     Card card1 = this.getCards().get(0);
    //     Card card2 = this.getCards().get(1);
        
        
    // }

    
}
