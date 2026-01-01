package ch2_basicBV.BigTwo.V3;

import ch2_basicBV.BigTwo.V3.game.Player;
import ch2_basicBV.BigTwo.V3.card.Deck;
import ch2_basicBV.BigTwo.V3.patterns.FullHouseHandler;
import ch2_basicBV.BigTwo.V3.patterns.PairHandler;
import ch2_basicBV.BigTwo.V3.patterns.SingleHandler;
import ch2_basicBV.BigTwo.V3.patterns.StraightHandler;
import ch2_basicBV.BigTwo.V3.patterns.CardPatternHandler;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 建立責任鍊模式的牌型處理器 (Build card pattern handler chain using Chain of Responsibility)
        // 順序：Single -> Pair -> Straight -> FullHouse
        CardPatternHandler handlerChain = buildHandlerChain();
        
        // 建立大老二遊戲實例 (Create BigTwo game instance)
        Deck deck = new Deck();
        BigTwo bigTwo = new BigTwo(deck, handlerChain);
        
        // 玩家命名 (Player naming)
        Player[] players = new Player[4];
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== Big Two Game Initialization ===");
            for (int i = 0; i < players.length; i++) {
                System.out.print("Player " + i + " Enter player name: ");
                String name = scanner.nextLine();
                players[i] = new Player(name);
            }
            System.out.println();
            
            // 設定玩家並開始遊戲 (Set players and start game)
            bigTwo.setPlayers(players);
            bigTwo.start();
        }
    }

    /**
     * 建立牌型處理器責任鍊 (Build card pattern handler chain)
     * 順序：Single -> Pair -> Straight -> FullHouse
     */
    private static CardPatternHandler buildHandlerChain() {
        CardPatternHandler fullHouseHandler = new FullHouseHandler(null);
        CardPatternHandler straightHandler = new StraightHandler(fullHouseHandler);
        CardPatternHandler pairHandler = new PairHandler(straightHandler);
        CardPatternHandler singleHandler = new SingleHandler(pairHandler);
        return singleHandler;
    }
}
