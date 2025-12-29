package ch2_basicBV.BigTwo.V3;

import ch2_basicBV.BigTwo.V3.patterns.SingleHandler;
import ch2_basicBV.BigTwo.V3.patterns.PairHandler;
import ch2_basicBV.BigTwo.V3.patterns.StraightHandler;
import ch2_basicBV.BigTwo.V3.patterns.FullHouseHandler;

public class Main {
    public static void main(String[] args) {
        BigTwo bigTwo = new BigTwo(new SingleHandler(new PairHandler(new StraightHandler(new FullHouseHandler(null)))));
        
        Player[] players = new Player[4];
        String line = in.nextLine();
        for (int i = 0; i < players.length; i++) {
            String name = in.nextLine();
            players[i] = new Player(name);
        }
        
        bigTwo.start();
    }
}
