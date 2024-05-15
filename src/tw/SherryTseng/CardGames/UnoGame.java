package src.tw.SherryTseng.CardGames;

import java.util.ArrayList;

import javax.smartcardio.Card;

import javafx.scene.paint.Color;

public class UnoGame extends Game {
    private ArrayList<Card> cardList;

    @Override
    public void initialDeck() {
        System.out.println("UnoGame start with 40 cards");
        cardList = new ArrayList<>();
        for (Color color : Color.values()) {
            for (int number = 0; number <= 9; number++) {
                cardList.add(new Card(color, number));
            }
        }
    }

    @Override
    public void drawCard() {

    }
}
