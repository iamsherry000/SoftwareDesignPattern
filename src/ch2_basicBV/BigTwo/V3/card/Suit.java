package ch2_basicBV.BigTwo.V3.card;

public enum Suit {
    CLUB(0), DIAMOND(1), HEART(2), SPADE(3);
    //梅花,菱形,愛心,黑桃

    private final int value;

    Suit(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public String getShortName() {
        switch (this) {
            case CLUB: return "C";
            case DIAMOND: return "D";
            case HEART: return "H";
            case SPADE: return "S";
            default: return "?";
        }
    }
}