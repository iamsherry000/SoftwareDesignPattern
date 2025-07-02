package TemplateMethod.practice;

public abstract class Card {

    @Override
    public abstract String toString();

    public abstract boolean canPlayOn(Card topCard);

    ///  compareTo 只有 PokerCard 會用
    // public abstract int compareTo(Card otherCard);
}
