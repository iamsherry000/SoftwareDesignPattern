package ch2_basicBV.BigTwo.V3.patterns;

import static java.util.Objects.requireNonNullElse;

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
}
