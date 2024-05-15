package src.tw.SherryTseng.CardGames;

import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cardList;

    public Deck(List<Card> cardList) {
        this.cardList = cardList;
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public void shuffle() {
        Collections.shuffle(cardList);
    }

    public Card drawCard() {
        if (cardList.isEmpty()) {
            throw new IllegalStateException("Deck is empty. Cannot draw card.");
        }
        return cardList.remove(0); // Remove and return the top card from the deck
    }
}
