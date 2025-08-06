package BigTwo.model;

import java.util.List;

public class Player {
    private String name;
    private Hand hand = new Hand();

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Hand getHand() {
        hand.getOrderedHand();
        return hand;
    }

    public void addCardToHand(Card card) {
        hand.addCard(card);
    }

    public boolean hasCard(Card card) {
        return hand.contains(card);
    }

    public List<Card> playCards() {
        List<Card> playedCards = hand.getOrderedHand();
        return playedCards;
    }

    public void addCard(Card card) {
        hand.addCard(card);
    }
}
