package TemplateMethod.practice;

import java.util.List;
import java.util.Random;

public class AIPlayer extends Player{
    private static final Random random = new Random();
    private static int aiCount = 0;

    public AIPlayer() {
        super();
    }

    @Override
    public void setName() {
        this.name = "AI_" + (++aiCount);
    }

    @Override
    public Card takeTurn(List<Card> playableCards) {
        if (playableCards.isEmpty()) {
            // 無牌可出
            return null;
        }
        // 隨機出一張可出的牌（或直接出第一張）
        Card cardToPlay = playableCards.get(0); // 可以 random
        //System.out.println(getName() + " 出牌: " + cardToPlay);
        hand.remove(cardToPlay);
        return cardToPlay;
    }

}
