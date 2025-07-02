package TemplateMethod.practice;

import java.util.ArrayList;
import java.util.List;

public abstract class Game {
    protected List<Player> players;
    protected Deck deck;
    // protected int playerCount = 4;

    public Game(int humanPlayerCount) {
        players = new ArrayList<>();
        deck = new Deck();
        int totalPlayers = 4;

        for(int i = 0 ; i < totalPlayers ; i++) {
            if (i < humanPlayerCount) {
                //System.out.println("Create HumanPlayer, i=" + i);
                players.add(new HumanPlayer());
            } else {
                //System.out.println("Create AIPlayer, i=" + i);
                players.add(new AIPlayer());
            }
        }
    }

    /// ===== Template Method 主流程 =====
    public final void startGame() {
        namePlayers();
        deck.shuffle();
        drawCards();
        gameLoop();
        announceWinner();
    }

    protected void namePlayers() { ///  多型，每個 Player 自己負責命名
        for (Player player : players) {
            player.setName();
        }
    }

    protected abstract void drawCards();
    protected abstract void gameLoop();
    protected abstract void announceWinner();
}
