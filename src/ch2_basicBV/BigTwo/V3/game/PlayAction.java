package ch2_basicBV.BigTwo.V3.game;

import ch2_basicBV.BigTwo.V3.card.Card;
import java.util.List;
import java.util.Collections;

/**
 * Represents a player's action - either playing cards or passing
 */
public class PlayAction {
    private final boolean isPass;
    private final List<Card> cards;

    private PlayAction(boolean isPass, List<Card> cards) {
        this.isPass = isPass;
        this.cards = cards != null ? cards : Collections.emptyList();
    }

    /**
     * Create a pass action
     */
    public static PlayAction pass() {
        return new PlayAction(true, null);
    }

    /**
     * Create a play cards action
     */
    public static PlayAction playCards(List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            throw new IllegalArgumentException("Cards cannot be null or empty for play action");
        }
        return new PlayAction(false, cards);
    }

    public boolean isPass() {
        return isPass;
    }

    public boolean isPlay() {
        return !isPass;
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    @Override
    public String toString() {
        if (isPass) {
            return "PASS";
        } else {
            return "PLAY: " + cards;
        }
    }
}
