/*
* CardPattern : responsible for storing the cards. 
* I was thinking if I should make a "AbstractCardPattern", but think about the flexibility, I didn't.
* 
*/

package cardPattern;

import card.Card;
import java.util.List;

public class CardPattern {
    private final List<Card> cards;

    public CardPattern(List<Card> cards) {
        this.cards = cards;
    }

    // Getter 方法：讓子類別或外部能存取牌組
    public List<Card> getCards() {
        return cards;
    }

    // 取得牌型的牌數
    public int getSize() {
        return cards.size();
    }
}
