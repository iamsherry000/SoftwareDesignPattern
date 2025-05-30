package abstractClass;

import static abstractClass.Decision.*;
import java.util.Map;

public class Game {
    private Player player1;
    private Player player2;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        if(player1.getNumber() == player2.getNumber()) {
            throw new IllegalArgumentException("Player1 and Player2 cannot be same.");
        }
    }

    private final static Map<Decision, Decision> counterMap =
            Map.of(SCISSORS, PAPER,
                    PAPER, ROCK,
                    ROCK, SCISSORS);

    public void start() {
        System.out.println("Game started.");

        Decision p1Decision = player1.decide();
        Decision p2Decision = player2.decide();

        System.out.printf("Player1: %s, Player2: %s\n", p1Decision, p2Decision);

        if(p1Decision == p2Decision) {
            System.out.println("It is a draw.");
        } else if(counterMap.get(p1Decision) == p2Decision) {
            System.out.println("Player1 wins.");
        } else {
            System.out.println("Player2 wins.");
        }
    }
}
