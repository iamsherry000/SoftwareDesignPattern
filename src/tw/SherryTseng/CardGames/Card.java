package src.tw.SherryTseng.CardGames;

public class Card {
    // For Uno
    private Color color; 
    private Number number;   

    // For Showdown
    private Suit suit;    
    private Rank rank;    

    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    public enum Rank {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING
    }

    public enum Color {
        RED, BLUE, YELLOW, GREEN
    }
    
    // Constructor for Uno
    public Card(Color color, Number number) {
        this.color = color;
        this.number = number;
    }

    // Constructor for Showdown
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    // Getters for Uno
    public Color getColor() {
        return color;
    }

    public Number getNumber() {
        return number;
    }

    // Getters for Showdown
    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

}
