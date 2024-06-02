package src.tw.SherryTseng.BigTwo;

public class Card {
    private Suit suit;    
    private Rank rank; 

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    // public enum Suit {
    //     ♣,♦,♥,♠
    // }

    // public enum Rank {
    //     A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K
    // }

    public enum Suit {
        // CLUBS("♣"), DIAMONDS("♦"), HEARTS("♥"),SPADES("♠");
        CLUBS("\u1F0D"), DIAMONDS("\u1F0C"), HEARTS("\u1F0B"),SPADES("\u1F0A");

        private final String symbol;

        Suit(String symbol) {
            this.symbol = symbol;
        }

        // @Override
        // public String toString() {
        //     return symbol;
        // }

        // public String getSymbol() {
        //     return this.symbol;
        // }
    }

    public enum Rank {
        ACE("A"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"), JACK("J"), QUEEN("Q"), KING("K");

        private final String symbol;

        Rank(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return symbol;
        }
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return rank + " " + suit;
        // return rank + " " + suit.getSymbol();
    }
}
