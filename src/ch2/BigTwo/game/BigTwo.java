package BigTwo.game;

import java.util.Scanner;
import BigTwo.model.*;
import BigTwo.pattern.*;
import BigTwo.pattern.pair.PairHandler;
import BigTwo.pattern.single.SingleHandler;
import BigTwo.pattern.straight.StraightHandler;
import BigTwo.pattern.fullHouse.FullHouseHandler;


public class BigTwo {
    public static void main(String[] args) {
        int PLAYER_LENGTH = 4;
        Player[] players = new Player[PLAYER_LENGTH];
        Deck deck = new Deck();
        int firstPlayerIndex = -1;
        CardPattern topPlayPattern = null;
        int passCount = 0;
        boolean isGameOver = false;

        // askPlayerName(PLAYER_LENGTH, players);
        defaultPlayerName(players);

        /// -- Game start --
        System.out.println("Welcome to Big Two!");
        deck.shuffle();
        playerGetCards(players, deck); // 發牌
        firstPlayerIndex = findFirstPlayer(players); // 找出第一個玩家
        System.out.println(firstPlayerIndex + " is the first player");

        /// -- Game loop --
        while(!isGameOver) {
            Player currentPlayer = players[firstPlayerIndex];
            System.out.println("Current player: " + currentPlayer.getName());
            System.out.println("Your hand: " + currentPlayer.getHand());

            // check if the player has cards to play
            if (currentPlayer.getHand().isEmpty()) {
                System.out.println(currentPlayer.getName() + " has no cards left!");
                isGameOver = true;
                break;
            }

            // ask player to play or pass
            System.out.println("Please enter your play (or type 'pass' to pass):");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine().trim();

        }
    }

    public static void askPlayerName(Player[] players) {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < players.length; i++) {
            System.out.print("Player" + i + "'s name：");
            String name = sc.nextLine();
            players[i] = new Player(name);
        }
    }

    public static void defaultPlayerName(Player[] players) {
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player("Player" + (i + 1));
        }
    }

    public static void playerGetCards(Player[] players, Deck deck) {
        int tempIndex = 0;
        // add cards to players' hands
        while(!deck.isEmpty()) {
            players[tempIndex % players.length].addCardToHand(deck.draw());
            tempIndex ++;
        }
    }

    public static int findFirstPlayer(Player[] players) {
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

    public static void setHandlerChain() {
        PatternHandler single = new SingleHandler();
        PatternHandler pair = new PairHandler();
        PatternHandler straight = new StraightHandler();
        PatternHandler fullHouse = new FullHouseHandler();

        single.setNext(pair);
        pair.setNext(straight);
        straight.setNext(fullHouse);
    }

}
