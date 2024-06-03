import java.util.ArrayList;
import java.util.List;

public abstract class Game {
    protected List<Player> playerList; // stored all the player
    protected Deck deck; 
    private static int PLAYERNUM = 4; // The total number of player

    public Game() {
        this.playerList = new ArrayList<>(); 
    }

    public void start() {
        initialPlayer();
        initialDeck();
        drawCards();
        takeTurn();
    }

    public void initialPlayer() {
        System.out.println("Intializing the player");
        for(int i = 0; i < PLAYERNUM; i++) {
            if (i == 0) {
                HumanPlayer humanPlayer = new HumanPlayer("");
                humanPlayer.nameHimself();
                playerList.add(humanPlayer);
            } else {
                AIPlayer aiPlayer = new AIPlayer();
                aiPlayer.nameHimself();
                playerList.add(aiPlayer);
            }
            System.out.println("Player " + (i+1) + ": " + playerList.get(i).getName()); 
        }
    }

    public abstract void initialDeck(); 

    public abstract void drawCards();

    public abstract void takeTurn();

}
