package game.base;

import java.util.List;

import static game.utils.Utils.printf;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public abstract class CardGame<Player extends game.base.Player<Card>, Card> {
    protected List<Player> players;
    protected final Deck<Card> deck;
    protected Player turnPlayer;
    protected int turn = 0;
    protected int round = 0;

    public CardGame(Deck<Card> deck, List<Player> players) {
        this.players = players;
        this.deck = deck;
    }

    public void start() {
        nameThemselves();
        drawHands();
        onGameBegins();
        nextTurn();
    }

    protected void onGameBegins() {
        // hook
    }

    private void nameThemselves() {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).nameSelf(i + 1);
        }
    }

    private void drawHands() {
        int initialHandSize = getInitialHandSize();
        for (int i = 0; i < initialHandSize; i++) {
            for (Player player : players) {
                Card card = deck.draw();
                player.addHandCard(card);
            }
        }
    }

    protected void nextTurn() {
        turnPlayer = players.get(turn % players.size());
        takeTurn(turnPlayer);
        turn ++;
        if (turn % players.size() == 0) {
            round ++;
            onRoundEnd();
        }
        if (isGameOver(round)) {
            gameOver();
        } else {
            nextTurn();
        }
    }

    private void gameOver() {
        Player winner = getWinner();
        printf("The winner is %s.\n", winner.getName());
    }

    protected abstract int getInitialHandSize();

    protected abstract void takeTurn(Player nextPlayer);

    protected void onRoundEnd() {}

    protected abstract boolean isGameOver(int currentRound);

    protected abstract Player getWinner();
}
