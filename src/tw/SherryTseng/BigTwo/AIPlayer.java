package src.tw.SherryTseng.BigTwo;

import java.util.ArrayList;
import java.util.Random;

public class AIPlayer extends Player{
    private static ArrayList<String> names = new ArrayList<>();

    public AIPlayer() {
        super("");
    }

    @Override
    public void nameHimself() {
        this.name = generateUniqueName();
    }

    private static String generateUniqueName() {
        Random random = new Random();
        String playerName;
        do {
            int randomNumber = random.nextInt(10) + 1;
            playerName = "AIPlayer" + randomNumber;
        } while (names.contains(playerName));
        names.add(playerName);
        return playerName;
    }
}
