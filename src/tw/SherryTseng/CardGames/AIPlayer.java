package src.tw.SherryTseng.CardGames;

import java.util.ArrayList;
import java.util.Random;

public class AIPlayer extends Player{
    private static ArrayList<String> names = new ArrayList<>();;
    
    public AIPlayer() {
        super(generateUniqueName());
    }

    @Override
    public void nameHimself() {
        super.name = generateUniqueName();
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
