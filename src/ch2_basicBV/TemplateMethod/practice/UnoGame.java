package TemplateMethod.practice;

import java.util.*;

public class UnoGame extends Game{
    private List<Card> tableCards = new ArrayList<>();
    private boolean gameEnded = false;

    public UnoGame(int humanPlayerCount) {
        super(humanPlayerCount);
        deck = buildUnoDeck();
    }

    // 建立 40 張 UNO 牌
    private Deck buildUnoDeck() {
        Deck unoDeck = new Deck();
        for (UnoColor color : UnoColor.values()) {
            for (int n = 0; n <= 9; n++) {
                unoDeck.cards.add(new UnoCard(color, n));
            }
        }
        return unoDeck;
    }

    @Override
    protected void drawCards() {
        for (int i = 0; i < 5; i++) {
            for (Player player : players) {
                player.draw(deck);
            }
        }
    }

    @Override
    protected void gameLoop() {
        // 檯面上先翻一張
        Card topCard = deck.draw();
        tableCards.add(topCard);
        System.out.println("\n Start with：" + topCard);

        int currentPlayerIdx = 0;
        while (!gameEnded) {
            Player current = players.get(currentPlayerIdx);
            topCard = tableCards.get(tableCards.size() - 1);

            List<Card> playable = current.getHand().getPlayableCards(topCard);

            if (playable.isEmpty()) {
                System.out.println("[" + current.getName() + "] 沒有可出的牌，必須抽牌。");
                // 連抽直到抽到可出的牌，並提示每抽一次
                boolean drewPlayable = false;
                while (!drewPlayable) {
                    if (deck.isEmpty()) {
                        // 洗牌
                        List<Card> recycled = new ArrayList<>(tableCards.subList(0, tableCards.size() - 1));
                        tableCards = new ArrayList<>(tableCards.subList(tableCards.size() - 1, tableCards.size()));
                        deck.reset(recycled);
                        System.out.println("牌堆空了，回收檯面牌並洗牌。");
                    }
                    System.out.println("[" + current.getName() + "] 從牌堆抽一張牌...");
                    current.draw(deck);
                    // 延遲 0.5 秒（非必要，增強 UX）
                    try { Thread.sleep(500); } catch (InterruptedException e) {}
                    // 再檢查一次
                    playable = current.getHand().getPlayableCards(topCard);
                    if (!playable.isEmpty()) {
                        drewPlayable = true;
                        System.out.print("[" + current.getName() + "] 現在可出：");
                        for (Card card : playable) {
                            System.out.print(card + " ");
                        }
                        System.out.println();
                    }
                }
            }

            // 此時必定有 playable
            Card played = current.takeTurn(playable);
            tableCards.add(played);
            System.out.println(current.getName() + " 出牌: " + played);

            // 判斷是否有人贏
            if (current.getHand().isEmpty()) {
                System.out.println("\n" + current.getName() + " 出完所有牌，獲勝！");
                gameEnded = true;
                break;
            }
            currentPlayerIdx = (currentPlayerIdx + 1) % players.size();
        }
    }

    @Override
    protected void announceWinner() {
        // 已在 gameLoop 宣布勝者（誰先出完誰贏）
        System.out.println("\nUNO 遊戲結束！");
    }
}
