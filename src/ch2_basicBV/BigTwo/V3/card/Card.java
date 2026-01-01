package ch2_basicBV.BigTwo.V3.card;

public class Card {
    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }
    
    public Rank getRank() {
        return rank;
    }

    public String toString() {
        return suit.getShortName() + rank.getShortName();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return suit == card.suit && rank == card.rank;
    }

    @Override
    public int hashCode() {
        return suit.hashCode() * 31 + rank.hashCode();
    }
}
