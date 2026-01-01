package ch2_basicBV.BigTwo.V3;

import ch2_basicBV.BigTwo.V3.card.Deck;
import ch2_basicBV.BigTwo.V3.card.Card;

import ch2_basicBV.BigTwo.V3.game.Player;
import ch2_basicBV.BigTwo.V3.game.PlayAction;
import ch2_basicBV.BigTwo.V3.game.CardPlay;
import ch2_basicBV.BigTwo.V3.patterns.CardPatternHandler;
import ch2_basicBV.BigTwo.V3.patterns.CardPattern;

import static java.util.Arrays.stream;

public class BigTwo {
    private final Deck deck;
    private final CardPatternHandler cardPatternHandler;
    private Player[] players;
    private boolean firstRound = true;
    private int consecutivePass = 0;
    private CardPlay topPlay;

    public BigTwo(Deck deck, CardPatternHandler cardPatternHandler) {
        this.deck = deck;
        this.cardPatternHandler = cardPatternHandler;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public void start() {
        System.out.println("BigTwo started");
        distributeHandCards();
        Player firstPlayer = findFirstPlayerWhoHasClub3();
        startNewRound(firstPlayer);
    }

    private void distributeHandCards() {
        deck.shuffle();
        int i = 0;
        while (!deck.isEmpty()) {
            Card card = deck.draw();
            players[i % 4].addHandCard(card);
            i++;
        }
    }

    private Player findFirstPlayerWhoHasClub3() {
        return stream(players)
                .filter(Player::hasClub3)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No player has Club 3"));
    }

    private void startNewRound() {
        startNewRound(topPlay.getPlayer());
    }

    private void startNewRound(Player topPlayer) {
        consecutivePass = 0;
        topPlay = null;
        System.out.println("New round started.");
        startNewTurn(topPlayer);
    }

    private void startNewTurn(Player nextPlayer) {
        askPlayerToPlay(nextPlayer, String.format("It's %s's turn", nextPlayer.getName()));
    }

    private void askPlayerToPlay(Player player, String message) {
        System.out.println(message);
        PlayAction action = player.play();

        if (action.isPass()) {
            if (isNewRound()) {
                askPlayerToPlay(player, "You cannot pass in a new round");
            } else {
                pass(player);
            }
        } else {
            validateCardsAndPlay(player, action);
        }
    }

    private void pass(Player player) {
        System.out.printf("Player %s PASS.\n", player.getName());
        consecutivePass++;
        if (hasThreeConsecutivePasses()) {
            startNewRound();
        } else {
            startNewTurn(nextPlayer(player));
        }
    }

    private boolean hasThreeConsecutivePasses() {
        return consecutivePass == 3;
    }

    private void validateCardsAndPlay(Player player, PlayAction action) {
        CardPattern pattern = cardPatternHandler.handle(action.getCards());
        if (pattern != null) {
            CardPlay cardPlay = new CardPlay(player, pattern);
            if (isValidPlay(cardPlay)) {
                play(cardPlay);
            } else {
                askPlayerToPlay(player, "Invalid play, please try again.");
            }
        } else {
            askPlayerToPlay(player, "Invalid card pattern, please try again.");
        }
    }

    private boolean isValidPlay(CardPlay play) {
        return mustPlayClub3AtFirstRound(play) &&
                (isNewRound() || mustHaveSameCardPatternTypeAndBeGreaterThanTopPlay(play));
    }

    private boolean mustPlayClub3AtFirstRound(CardPlay play) {
        return !isFirstRound() || play.containsClub3();
    }

    private boolean mustHaveSameCardPatternTypeAndBeGreaterThanTopPlay(CardPlay play) {
        return play.getCardPattern().isSamePattern(topPlay.getCardPattern()) &&
                play.isGreaterThan(topPlay);
    }

    private void play(CardPlay cardPlay) {
        placeNewTopPlay(cardPlay);
        Player player = topPlay.getPlayer();
        player.loseHandCards(cardPlay.getCards());
        if (player.hasNoHandCards()) {
            System.out.printf("Game Over! Winner: %s\n", player.getName());
        } else {
            startNewTurn(nextPlayer(player));
        }
    }

    private void placeNewTopPlay(CardPlay cardPlay) {
        System.out.println(cardPlay);
        consecutivePass = 0;
        firstRound = false;
        topPlay = cardPlay;
    }

    public boolean isNewRound() {
        return topPlay == null;
    }

    public boolean isFirstRound() {
        return firstRound;
    }
    private Player nextPlayer(Player player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == player) {
                return players[(i + 1) % players.length];
            }
        }
        throw new IllegalStateException("Player not found in players array");
    }
}
