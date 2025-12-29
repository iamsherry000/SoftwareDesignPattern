package ch2_basicBV.BigTwo.V3;

import ch2_basicBV.BigTwo.V3.patterns.CardPatternHandler;

public class BigTwo {
    private final CardPatternHandler handlerChain;
    // private final Deck deck;

    public BigTwo(CardPatternHandler handlerChain) {
        this.handlerChain = handlerChain;
    }

    public void start() {
        System.out.println("BigTwo started");
    }
}
