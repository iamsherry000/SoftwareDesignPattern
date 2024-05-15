package src.tw.SherryTseng.CardGames;

import java.util.List;

public abstract class Player {
    protected String name;
    protected Hand hand;

    // Constructor
    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
    }

    // getter
    public String getName() { 
        return name;
    }

    public abstract void nameHimself();

    public void addCardToHand(Card card) {
        hand.addCard(card);
    }
}
