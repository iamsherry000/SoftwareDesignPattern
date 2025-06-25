package TemplateMethod.practice;

public class Main {
    public static void main(String[] args) {
        PokerCard c1 = new PokerCard(Rank.ACE, Suit.SPADE);
        PokerCard c2 = new PokerCard(Rank.TEN, Suit.HEART);
        PokerCard c3 = new PokerCard(Rank.TEN, Suit.CLUB);
        UnoCard c4 = new UnoCard(UnoColor.BLUE, 12);
        UnoCard c5 = new UnoCard(UnoColor.BLUE, 14);

        System.out.println(c1);
        System.out.println(c2);
        System.out.println(c3);
        System.out.println(c4);
        System.out.println(c5);
    }
}
