package src.tw.SherryTseng.CardGames;

import java.util.ArrayList;

public class ShowdownGame extends Game{
    private ArrayList<Card> cardList;

    @Override
    public void initialDeck() {
        System.out.println("ShowdownGame start with 52 cards");
        cardList = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cardList.add(new Card(suit, rank));
            }
        }
    }

    @Override
    public void drawCard() {

    }
}
