package TemplateMethod.practice;

import java.util.Scanner;
import java.util.List;

public class HumanPlayer extends Player{
    private final Scanner scanner = new Scanner(System.in);

    public HumanPlayer() {
        super();
    }

    @Override
    public void setName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Write down your name：");
        this.name = scanner.nextLine();
    }

    @Override
    public Card takeTurn(List<Card> playableCards) {
        List<Card> allCards = hand.getCards();
        System.out.printf("\n[%s] 目前有 %d 張手牌：\n", name, allCards.size());
        for (int i = 0; i < allCards.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, allCards.get(i));
        }

        // 只有 playableCards.size() < allCards.size() 時（＝有不能出的牌）才顯示「可以出的有」
        if (playableCards.size() < allCards.size()) {
            if (playableCards.isEmpty()) {
                System.out.println("（此回合無任何可出的牌，將自動抽牌！）");
                return null;
            }
            System.out.print("可以出的有：");
            for (Card card : playableCards) {
                System.out.print(card + " ");
            }
            System.out.println();
        }

        // 選牌
        int index = -1;
        while (true) {
            System.out.print("請輸入可出的牌編號（1-" + playableCards.size() + "）：");
            try {
                index = Integer.parseInt(scanner.nextLine());
                if (index < 1 || index > playableCards.size()) {
                    System.out.print("輸入錯誤。可以選：");
                    for (int i = 1; i <= playableCards.size(); i++) {
                        System.out.print(i + (i == playableCards.size() ? "\n" : ", "));
                    }
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("請輸入數字。");
            }
        }
        Card cardToPlay = playableCards.get(index - 1);
        hand.remove(cardToPlay);
        return cardToPlay;
    }

    // 輔助顯示
    private String validChoicesString(int size) {
        if (size == 1) return "1";
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= size; i++) {
            sb.append(i);
            if (i < size) sb.append(", ");
        }
        return sb.toString();
    }

}
