package card;

public class Card {
<<<<<<< HEAD
    private Suit suit;
    private Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
=======
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
        return rank + " of " + suit;
>>>>>>> 5787cea11935d7a2048a58f219b241d934c659e3
    }
}
