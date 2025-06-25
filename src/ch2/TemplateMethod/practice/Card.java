package TemplateMethod.practice;

public abstract class Card {

    @Override
    public abstract String toString();

    public abstract boolean canPlayOn(Card topCard);
}
