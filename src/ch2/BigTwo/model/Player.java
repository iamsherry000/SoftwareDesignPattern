package BigTwo.model;

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
        return hand;
    }

    public void addCardToHand(Card card) {
        hand.addCard(card);
    }

    public boolean hasCard(Card card) {
        return hand.contains(card);
    }
}
