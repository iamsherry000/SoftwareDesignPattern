package ch2_basicBV.BigTwo.V3.game;

import ch2_basicBV.BigTwo.V3.card.Card;
import ch2_basicBV.BigTwo.V3.card.Suit;
import ch2_basicBV.BigTwo.V3.card.Rank;
import ch2_basicBV.BigTwo.V3.patterns.CardPattern;

import java.util.List;

/**
 * Represents a card play action by a player with a specific pattern
 */
public class CardPlay {
    private final Player player;
    private final CardPattern cardPattern;
    private final List<Card> cards;

    public CardPlay(Player player, CardPattern cardPattern) {
        this.player = player;
        this.cardPattern = cardPattern;
        this.cards = cardPattern.getCards();
    }

    public Player getPlayer() {
        return player;
    }

    public CardPattern getCardPattern() {
        return cardPattern;
    }

    public List<Card> getCards() {
        return cards;
    }

    /**
     * Check if this play contains Club 3 (required for first round)
     */
    public boolean containsClub3() {
        Card club3 = new Card(Suit.CLUB, Rank.THREE);
        return cards.contains(club3);
    }

    /**
     * Check if this play is greater than another play
     * (simplified implementation - should be enhanced based on Big Two rules)
     */
    public boolean isGreaterThan(CardPlay other) {
        if (other == null) {
            return true;
        }
        
        // Must be same pattern type
        if (!cardPattern.isSamePattern(other.cardPattern)) {
            return false;
        }
        
        // Simplified comparison - in real Big Two, this would be more complex
        // For now, we'll allow any play of the same pattern type
        return true;
    }

    @Override
    public String toString() {
        return String.format("Player %s played %s: %s", 
            player.getName(), 
            cardPattern.getName(), 
            cards);
    }
}
