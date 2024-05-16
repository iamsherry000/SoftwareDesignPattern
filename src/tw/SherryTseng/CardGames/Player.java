package src.tw.SherryTseng.CardGames;

import java.util.List;

public abstract class Player {
    protected String name;
    protected Hand hand;
    private int totalPoints;

    // Constructor
    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
        this.totalPoints = 0;
    }

    // getter
    public String getName() { 
        return name;
    }

    public abstract void nameHimself();

    public void addCardToHand(Card card) {
        hand.addCard(card);
    }

    public abstract Card playCard();

    public void addPoint() {
        this.totalPoints++;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public Hand getHand() {
        return hand;
    }

    public boolean isHandEmpty() {
        return hand.isEmpty();
    }
}
