package src.tw.SherryTseng.CardGames;

import java.util.ArrayList;
import java.util.Random;

public class AIPlayer extends Player{
    private static ArrayList<String> names = new ArrayList<>();
    private Random random;
    
    public AIPlayer() {
        super(generateUniqueName());
        this.random = new Random();
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

    @Override
    public Card playCard() {
        // AI player randomly chooses a card from their hand
        int index = random.nextInt(hand.size());
        return hand.removeCard(index);
    }
}
