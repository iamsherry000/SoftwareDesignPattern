package BigTwo.game;

import BigTwo.model.Player;
import BigTwo.model.Deck;
import BigTwo.model.Card;
import java.util.Scanner;

public class BigTwo {
    public static void main(String[] args) {
        int PLAYER_LENGTH = 4;
        Player[] players = new Player[PLAYER_LENGTH];
        Deck deck = new Deck();
        int firstPlayerIndex = -1;

        // askPlayerName(PLAYER_LENGTH, players);
        defaultPlayerName(players);

        /// -- Game start --
        System.out.println("Welcome to Big Two!");
        deck.shuffle();
        playerGetCards(players, deck);
        firstPlayerIndex = findFirstPlayer(players);

        /// -- Game loop --
        // Player play cards (start from the first player)
            // choose cards to play
        // compare to current cards
            // if valid, play the cards
            // if not valid, pass
        //
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


}
