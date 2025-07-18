package BigTwo.model;

public class Card {
    private int suit;  // 0: Club♣, 1: Diamond♦, 2: Heart♥, 3: Spade♠
    private int rank;  // 1 - 13 (A - K)

    private static final String[] SUIT_NAMES = {"Clubs", "Diamonds", "Hearts", "Spades"};
    private static final String[] RANK_NAMES = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    public Card(int suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Card(String suit, String rank) {
        this.suit = parseIndex(SUIT_NAMES, suit);
        this.rank = parseIndex(RANK_NAMES, rank);
    }

    private static int parseIndex(String[] arr, String value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equalsIgnoreCase(value)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }

    public int getSuit() {
        return suit;
    }

    public int getRank() {
        return rank;
    }

    public String toString() {
        String[] suits = {"♣", "♦", "♥", "♠"};
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        return ranks[rank] + suits[suit];
    }

    // Card.java
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return suit == card.suit && rank == card.rank;
    }

    @Override
    public int hashCode() {
        return 31 * suit + rank;
    }
}
