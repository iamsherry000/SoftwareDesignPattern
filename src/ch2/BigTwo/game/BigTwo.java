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
        PatternHandler handlerChain = setHandlerChain();

        // askPlayerName(players); // 讓玩家輸入名字
        defaultPlayerName(players); // 讓玩家default名字(測試)
        deck.shuffle();// 洗牌
        playerGetCards(players, deck);// 發牌

        int firstPlayerIndex = findFirstPlayer(players);
        int currentPlayerIndex = firstPlayerIndex;
        CardPattern topPlayPattern = null;
        int passCount = 0;
        boolean isGameOver = false;
        boolean isFirstRound = true;

        System.out.println("Welcome to Big Two!");

        while (!isGameOver) {
            Player currentPlayer = players[currentPlayerIndex]; // Get the current player
            System.out.println("輪到" + currentPlayer.getName() + "了");
            printCurrentHand(currentPlayer); // Print the player's hand

            boolean validMove = false; // Track if the player made a valid move
            while (!validMove) {
                String input = getPlayerInput(currentPlayer, topPlayPattern);
                if (input.equalsIgnoreCase("pass")) {
                    if (handlePass(currentPlayer, topPlayPattern)) {
                        passCount++;
                        validMove = true; // Allow the player to pass
                    }
                } else {
                    List<Card> playedCards = parsePlayedCards(input, currentPlayer);
                    if (playedCards != null) {
                        CardPattern currentPattern = handlerChain.recognize(playedCards);
                        if (handlePlay(currentPlayer, playedCards, currentPattern, topPlayPattern, isFirstRound)) {
                            isFirstRound = false;
                            topPlayPattern = currentPattern;
                            passCount = 0;
                            validMove = true; // Valid play, exit the loop
                        }
                    }
                }
            }

            if (currentPlayer.getHand().isEmpty()) {
                System.out.println("遊戲結束，遊戲的勝利者為 " + currentPlayer.getName());
                isGameOver = true;
            } else if (passCount == PLAYER_LENGTH - 1) {
                // 三人 PASS，回合重啟
                System.out.println("新的回合開始了。");
                topPlayPattern = null;
                passCount = 0;
                isFirstRound = false;
            }

            currentPlayerIndex = (currentPlayerIndex + 1) % PLAYER_LENGTH; // Move to the next player
        }
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
        Card club3 = new Card(0, 0);
        for (int i = 0; i < players.length; i++) {
            if (players[i].hasCard(club3)) { // club3 is represented as suit 0, rank 2
                firstPlayerIndex = i;
                // System.out.println(players[i].getName() + " has club3!");
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

    private static String getPlayerInput(Player currentPlayer, CardPattern topPlayPattern) {
        Scanner sc = new Scanner(System.in);
        System.out.print(topPlayPattern == null ? "(Space needed) Play cards: " : "(Space needed) Play cards or pass: ");
        return sc.nextLine().trim();
    }

    private static boolean handlePass(Player player, CardPattern topPlayPattern) {
        if (topPlayPattern == null) {
            System.out.println("你不能在新的回合中喊 PASS");
            return false;
        }
        System.out.println("玩家 " + player.getName() + " PASS.");
        return true;
    }

    private static List<Card> parsePlayedCards(String input, Player player) {
        try {
            List<Card> playedCards = new ArrayList<>();
            String[] inputIndexes = input.split("\\s+");
            for (String index : inputIndexes) {
                playedCards.add(player.getHand().getCard(Integer.parseInt(index)));
            }
            return playedCards;
        } catch (Exception e) {
            System.out.println("此牌型不合法，請再嘗試一次。");
            return null;
        }
    }

    private static boolean handlePlay(Player player, List<Card> playedCards, CardPattern currentPattern, CardPattern topPlayPattern, boolean isFirstRound) {
        Card club3 = new Card(0, 0); // Club 3 is represented as suit 0, rank 2

        if (currentPattern == null) {
            System.out.println("此牌型不合法，請再嘗試一次。");
            return false;
        }

        // Check if it's the first round and the played cards contain Club 3
        if (isFirstRound && playedCards.stream().noneMatch(card -> card.equals(club3))) {
            System.out.println("第一輪必須包含梅花三，請再嘗試一次。");
            return false;
        }

        if (topPlayPattern == null || currentPattern.isGreaterThan(topPlayPattern)) {
            List<Card> sortedCards = currentPattern.getCards();

            String patternName = getPatternName(currentPattern);
            String cardsStr = sortedCards.stream()
                    .map(Card::toString)
                    .reduce((a, b) -> a + " " + b)
                    .orElse("");

            System.out.println("玩家 " + player.getName() + " 打出了 " + patternName + " " + cardsStr);
            player.getHand().removeCards(playedCards);
            return true;
        } else {
            System.out.println("此牌型不合法，請再嘗試一次。");
            return false;
        }
    }
}
