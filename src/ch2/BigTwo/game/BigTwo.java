package BigTwo.game;

import java.util.ArrayList;
import java.util.Scanner;
import BigTwo.model.*;
import BigTwo.pattern.*;
import BigTwo.pattern.pair.PairHandler;
import BigTwo.pattern.single.SingleHandler;
import BigTwo.pattern.straight.StraightHandler;
import BigTwo.pattern.fullHouse.FullHouseHandler;
import java.util.List;

public class BigTwo {
    public static void main(String[] args) {
        int PLAYER_LENGTH = 4;
        Player[] players = new Player[PLAYER_LENGTH];
        Deck deck = new Deck();
        int firstPlayerIndex = -1;
        CardPattern topPlayPattern = null;
        int passCount = 0;
        boolean isGameOver = false;
        PatternHandler handlerChain = setHandlerChain();

        // askPlayerName(players);
        defaultPlayerName(players); // 測試用

        /// -- Game start --
        System.out.println("Welcome to Big Two!");
        deck.shuffle(); // 洗牌
        playerGetCards(players, deck); // 發牌
        firstPlayerIndex = findFirstPlayer(players);
        int currentPlayerIndex = firstPlayerIndex;

//        while(!isGameOver) {
//            Player currentPlayer = players[currentPlayerIndex];
//            printCurrentHand(currentPlayer); // 輪到每一位玩家，輸出手牌
//
//        }

        Player currentPlayer = players[currentPlayerIndex];
        printCurrentHand(currentPlayer); // 輪到每一位玩家，輸出手牌
        System.out.print("(Space needed) Play cards: ");
        List<Card> playedCards = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        String[] inputIndexes = sc.nextLine().trim().split("\\s+");
        for (String input : inputIndexes) { // 讀取所有輸入
            int index = Integer.parseInt(input);
            playedCards.add(currentPlayer.getHand().getCard(index));
        }
        // Todo : 考慮 input pass

//        // print every card in playedCards
//        for (Card card : playedCards) {
//            System.out.print(card + " ");  // 預設 toString 格式為 S[3] H[4] 等
//        }
//        System.out.println(); // 換行

        // requirement 玩家 <玩家的名字> 打出了 <牌型名稱> <花色>[<數字>] <花色>[<數字>] <花色>[<數字>] ...
        CardPattern currentPattern = handlerChain.recognize(playedCards);
        System.out.println("Player " + currentPlayer.getName() + " 打出了 "
                + getPatternName(currentPattern) + " "
                + playedCards.stream()
                .map(Card::toString)
                .reduce((a, b) -> a + " " + b)
                .orElse(""));

        // 判斷牌型是否符合規則


    }

    private static void askPlayerName(Player[] players) {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < players.length; i++) {
            System.out.print("Player" + i + "'s name：");
            String name = sc.nextLine();
            players[i] = new Player(name);
        }
    }

    private static void defaultPlayerName(Player[] players) {
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player("Player" + (i + 1));
        }
    }

    private static void playerGetCards(Player[] players, Deck deck) {
        int tempIndex = 0;
        // add cards to players' hands
        while(!deck.isEmpty()) {
            players[tempIndex % players.length].addCardToHand(deck.draw());
            tempIndex ++;
        }
    }

    private static int findFirstPlayer(Player[] players) {
        int firstPlayerIndex = -1;
        Card club3 = new Card(0, 2);
        for (int i = 0; i < players.length; i++) {
            if (players[i].hasCard(club3)) { // club3 is represented as suit 0, rank 2
                firstPlayerIndex = i;
                System.out.println(players[i].getName() + " has club3!");
                break;
            }
        }
        return firstPlayerIndex;
    }

    private static PatternHandler setHandlerChain() {
        PatternHandler single = new SingleHandler();
        PatternHandler pair = new PairHandler();
        PatternHandler straight = new StraightHandler();
        PatternHandler fullHouse = new FullHouseHandler();

        single.setNext(pair);
        pair.setNext(straight);
        straight.setNext(fullHouse);

        return single;
    }

    private static void printCurrentHand(Player player) {
        List<Card> hand = player.getHand().getOrderedHand();

        // first line
        for(int i = 0 ; i < hand.size(); i++) {
            System.out.printf("%-5d", i);
        }
        System.out.println();

        // second line
        for(Card card: hand) {
            System.out.printf("%-5s", card.toString());
        }

        System.out.println();
    }

    private static String getPatternName(CardPattern pattern) {
        return switch (pattern.getClass().getSimpleName()) {
            case "SingleCardPattern" -> "單張";
            case "PairCardPattern" -> "對子";
            case "StraightCardPattern" -> "順子";
            case "FullHouseCardPattern" -> "葫蘆";
            default -> "未知牌型";
        };
    }
}
