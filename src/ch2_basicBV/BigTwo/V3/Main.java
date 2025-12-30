package ch2_basicBV.BigTwo.V3;

import ch2_basicBV.BigTwo.V3.game.Player;
import ch2_basicBV.BigTwo.V3.patterns.FullHouseHandler;
import ch2_basicBV.BigTwo.V3.patterns.PairHandler;
import ch2_basicBV.BigTwo.V3.patterns.SingleHandler;
import ch2_basicBV.BigTwo.V3.patterns.StraightHandler;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BigTwo bigTwo = new BigTwo(new SingleHandler(new PairHandler(new StraightHandler(new FullHouseHandler(null)))));
        
        Player[] players = new Player[4];
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < players.length; i++) {
            String name = scanner.nextLine();
            players[i] = new Player(name);
        }
        
        bigTwo.setPlayers(players);
        bigTwo.start();
    }
}
