package cardPatternHandlers;

import cardPatterns.CardPattern;
import card.Card;
import java.util.List;

/**
 * Chain of Responsibility Pattern - Abstract Handler
 * 每個 Handler 負責判斷一種牌型，若無法處理則傳給下一個 Handler
 */
public abstract class CardPatternHandler {
    protected CardPatternHandler next;

    public CardPatternHandler(CardPatternHandler next) {
        this.next = next;
    }

    /**
     * 嘗試將給定的牌組解析成對應的牌型
     * @param cards 玩家選擇的牌
     * @return 對應的 CardPattern，若無法匹配任何牌型則回傳 null
     */
    public CardPattern handle(List<Card> cards) {
        CardPattern pattern = doHandle(cards);
        if (pattern != null) {
            return pattern;
        }
        // 若自己無法處理，交給下一個 Handler
        if (next != null) {
            return next.handle(cards);
        }
        return null; // 沒有任何 Handler 能處理
    }

    /**
     * 子類別實作：嘗試將 cards 解析為特定牌型
     * @param cards 玩家選擇的牌
     * @return 若符合該牌型則回傳對應的 CardPattern，否則回傳 null
     */
    protected abstract CardPattern doHandle(List<Card> cards);
}
