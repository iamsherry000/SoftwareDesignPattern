package TemplateMethod.practice;

public class UnoCard extends Card{
    private final UnoColor  color; // 0: black 1: red
    private final int number; // 1 - 13

    public UnoCard(UnoColor color, int number) {
        this.color = color;
        this.number = number;
    }

    public UnoColor getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return color + " " + number;
    }

    @Override
    public boolean canPlayOn(Card topCard) {
        if (!(topCard instanceof UnoCard)) return false;
        UnoCard top = (UnoCard) topCard;
        return this.color == top.color || this.number == top.number;
    }


}
