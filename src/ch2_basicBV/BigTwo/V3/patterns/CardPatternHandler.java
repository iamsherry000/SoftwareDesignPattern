package ch2_basicBV.BigTwo.V3.patterns;

import static java.util.Objects.requireNonNullElse;
import java.util.List;
import ch2_basicBV.BigTwo.V3.card.Card;

public abstract class CardPatternHandler {
    private final CardPatternHandler next;

    // 這只是個普通 class，用不用都行，全看你自己。
    // public CardPatternHandler(CardPatternHandler next) {
    //     this.next = next;
    // } 
    
    // 這個 class 不能被外界隨便 new，但子類可以(參考 PairHandler)。
    protected CardPatternHandler() {
        this.next = null;
    }

    // Chain 的語意變成「一定會走到某個終點」
    public CardPatternHandler(CardPatternHandler next) {
        this.next = requireNonNullElse(next, NullCardPattern.INSTANCE);
    }

    /**
     * 處理牌型識別的責任鍊方法
     */
    public CardPattern handle(List<Card> cards) {
        CardPattern pattern = tryHandle(cards);
        if (pattern != null) {
            return pattern;
        }
        
        if (next != null && !(next instanceof NullCardPattern)) {
            return next.handle(cards);
        }
        
        return null; // 無法識別的牌型
    }

    /**
     * 子類別實作的具體處理邏輯
     */
    protected abstract CardPattern tryHandle(List<Card> cards);
}
