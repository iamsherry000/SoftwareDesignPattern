package TemplateMethod.practice;

import java.util.*;

public class ShowDownGame extends Game{
    private Map<Player, Integer> scores = new HashMap<>();

    public ShowDownGame(int humanPlayerCount) {
        super(humanPlayerCount);
        deck = buildPokerDeck();
    }

    /// 初始化一副 52 張撲克牌。
    private Deck buildPokerDeck() {
        Deck pokerDeck = new Deck();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                pokerDeck.cards.add(new PokerCard(rank, suit));
            }
        }
        return pokerDeck;
    }

    /// 4 人輪流抽 13 張牌，並初始化分數。
    @Override
    protected void drawCards() {
        // 輪流抽牌，直到每人 13 張
        for (int i = 0; i < 13; i++) {
            for (Player player : players) {
                player.draw(deck);
            }
        }
        // 初始化分數
        for (Player player : players) {
            scores.put(player, 0);
        }
    }

    /// 每回合所有人各出一張，最大牌贏得分。
    @Override
    protected void gameLoop() {
        System.out.println("\n=== 遊戲開始，共 13 回合！ ===");
        for (int round = 1; round <= 13; round++) {
            System.out.println("\n-- 第 " + round + " 回合 --");
            Map<Player, PokerCard> playedCards = new LinkedHashMap<>();
            for (Player player : players) {
                List<Card> handCards = player.getHand().getCards();
                PokerCard played = (PokerCard) player.takeTurn(handCards);
                playedCards.put(player, played);
                System.out.println(player.getName() + " 出牌: " + played);
            }

            // 判斷本回合最大牌
            Player roundWinner = null;
            PokerCard maxCard = null;
            for (Map.Entry<Player, PokerCard> entry : playedCards.entrySet()) {
                if (maxCard == null || entry.getValue().compareTo(maxCard) > 0) {
                    maxCard = entry.getValue();
                    roundWinner = entry.getKey();
                }
            }
            // 加分
            scores.put(roundWinner, scores.get(roundWinner) + 1);
            System.out.println("本回合贏家: " + roundWinner.getName() + "（" + maxCard + "）");
        }
    }

    /// 計算最終分數，公佈贏家。
    @Override
    protected void announceWinner() {
        System.out.println("\n=== 遊戲結束！各玩家分數： ===");
        int maxScore = Collections.max(scores.values());
        List<Player> winners = new ArrayList<>();
        for (Map.Entry<Player, Integer> entry : scores.entrySet()) {
            System.out.println(entry.getKey().getName() + ": " + entry.getValue());
            if (entry.getValue() == maxScore) {
                winners.add(entry.getKey());
            }
        }
        System.out.println("Winner of this round：" + winners.get(0).getName() + "！");
    }
}
