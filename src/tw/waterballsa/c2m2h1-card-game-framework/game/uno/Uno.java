package game.uno;

import game.base.CardGame;

import java.util.List;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Uno extends CardGame<Player, Card> {
    private final Deck discards = new Deck();
    private Card topCard;

    public Uno(Deck deck, List<Player> players) {
        super(deck, players);
    }

    @Override
    protected int getInitialHandSize() {
        return 5;
    }

    @Override
    protected void onGameBegins() {
        topCard = deck.draw();
    }

    @Override
    protected void takeTurn(Player player) {
        TurnMove turnMove = player.takeTurn(topCard);
        if (turnMove.isPass()) {
            pass(player);
        } else {
            if (isValidMove(turnMove)) {
                playCard(player, turnMove);
            } else {
                turnMove.undo();
            }
        }
    }

    private boolean isValidMove(TurnMove turnMove) {
        Card card = turnMove.getCard();
        return topCard.getColor() == card.getColor() ||
                topCard.getNumber() == card.getNumber();
    }

    private void pass(Player player) {
        System.out.printf("Player %s pass so he has to draw a card from the deck.\n", player.getName());
        reshuffleDeckIfEmpty();
        player.addHandCard(deck.draw());
    }

    private void playCard(Player player, TurnMove turnMove) {
        if (topCard != null) {
            discards.push(topCard);
        }
        topCard = turnMove.getCard();
        System.out.printf("Player %s plays a %s.\n", player.getName(), topCard);
    }

    private void reshuffleDeckIfEmpty() {
        if (deck.isEmpty()) {
            System.out.println("The deck is empty, reshuffling the deck.");
            deck.push(discards);
            discards.clear();
            deck.shuffle();
        }
    }

    @Override
    protected boolean isGameOver(int currentRound) {
        return turnPlayer.getHand().isEmpty();
    }

    @Override
    protected Player getWinner() {
        return turnPlayer;
    }
}
