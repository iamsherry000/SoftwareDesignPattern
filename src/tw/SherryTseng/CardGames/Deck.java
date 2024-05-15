package src.tw.SherryTseng.CardGames;

import java.util.ArrayList;
import java.util.Collections;

import javax.smartcardio.Card;

public class Deck {
    private ArrayList<Card> cards;

    public Deck() {
        this.cards = cards;
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }
}
