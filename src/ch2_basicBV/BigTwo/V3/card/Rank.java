package ch2_basicBV.BigTwo.V3.card;

public enum Rank {
    THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7),
    EIGHT(8), NINE(9), TEN(10),
    JACK(11), QUEEN(12), KING(13),
    ACE(1), TWO(2);

    private final int value;

    Rank(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public String getShortName() {
        switch (this) {
            case THREE: return "3";
            case FOUR: return "4";
            case FIVE: return "5";
            case SIX: return "6";
            case SEVEN: return "7";
            case EIGHT: return "8";
            case NINE: return "9";
            case TEN: return "10";
            case JACK: return "J";
            case QUEEN: return "Q";
            case KING: return "K";
            case ACE: return "A";
            case TWO: return "2";
            default: return "?";
        }
    }
}
