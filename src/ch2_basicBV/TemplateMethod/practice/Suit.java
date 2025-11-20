package TemplateMethod.practice;

public enum Suit {
    CLUB("Club", 1),
    DIAMOND("Diamond", 2),
    HEART("Heart", 3),
    SPADE("Spade", 4);

    private final String name;
    private final int value;

    Suit(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }
}
