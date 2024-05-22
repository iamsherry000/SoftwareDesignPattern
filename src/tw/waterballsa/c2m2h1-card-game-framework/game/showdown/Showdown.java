package game.showdown;

import game.base.CardGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.max;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.joining;

import static game.utils.Utils.print;
import static game.utils.Utils.printf;
import static game.utils.Utils.println;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Showdown extends CardGame<Player, Card> {
    public static final int NUM_OF_ROUNDS = 13;
    private final List<ShowCard> showCards = new ArrayList<>();

    public Showdown(Deck deck, List<Player> players) {
        super(deck, players);
    }

    @Override
    protected void takeTurn(Player player) {
        printf("It's %s's turn.\n", player.getName());
        Card card = player.takeTurn();
        showCards.add(new ShowCard(player, card));
    }

    @Override
    protected void onRoundEnd() {
        showdown();
        showCards.clear();
    }

    private void showdown() {
        printShowCards();
        ShowCard showCard = max(showCards);
        Player winner = showCard.getPlayer();
        winner.gainPoint();
        printf("%s wins this round.\n", winner.getName());
    }

    private void printShowCards() {
        print("Show cards: ");
        println(showCards.stream()
                .map(ShowCard::getCard)
                .map(Objects::toString).collect(joining(" ")));
    }

    @Override
    protected boolean isGameOver(int currentRound) {
        return currentRound >= NUM_OF_ROUNDS;
    }

    @Override
    protected Player getWinner() {
        return max(players, comparingInt(Player::getPoint));
    }

    @Override // 抽牌的終止條件
    protected int getInitialHandSize() {
        return 13;
    }
}
