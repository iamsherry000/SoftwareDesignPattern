package src.tw.SherryTseng.BigTwo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;
    
    public Deck() {
        cards = new ArrayList<>();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    // To make sure it's really shuffled
    public void showTopCards(int n) {
        for (int i = 0; i < n; i++) {
            System.out.println(cards.get(i));
        }
    }

    public void deal(List<Player> players) {
        int playerCount = players.size();
        int cardCount = cards.size();
        for (int i = 0; i < cardCount; i++) {
            players.get(i % playerCount).getHand().addCard(cards.get(i));
        }
    }

}
