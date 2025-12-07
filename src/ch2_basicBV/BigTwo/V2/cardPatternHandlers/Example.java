package cardPatternHandlers;

import cardPatterns.CardPattern;
import card.Card;
import java.util.List;

/**
 * 使用範例：展示如何建立和使用責任鏈
 */
public class Example {
    
    public static void main(String[] args) {
        // 建立責任鏈：FullHouse → Straight → Pair → Single
        // 注意：從最後一個往前建構
        CardPatternHandler handlerChain = new FullHouseHandler(
            new StraightHandler(
                new PairHandler(
                    new SingleHandler(null)
                )
            )
        );
        
        // 使用責任鏈解析牌型
        List<Card> cards = getCardsFromPlayer();
        CardPattern pattern = handlerChain.handle(cards);
        
        if (pattern != null) {
            System.out.println("辨識成功: " + pattern.getClass().getSimpleName());
        } else {
            System.out.println("無效的牌型組合");
        }
    }
    
    // 模擬方法
    private static List<Card> getCardsFromPlayer() {
        return null; // 實際實作中會從玩家手牌取得
    }
}

