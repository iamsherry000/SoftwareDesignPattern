package TemplateMethod.practice;

import java.util.List;
import java.util.Scanner;

public abstract class Player {
    protected String name;
    protected Hand hand;

    public Player() {
        this.hand = new Hand();
    } // UNO 用 new HumanPlayer()，Poker 用 new HumanPlayer(13)

    // for Poker
    public Player(int handMaxSize) {
        this.hand = new Hand(handMaxSize);
    }

    String getName() {
        return name;
    }

    public abstract void setName();

    public Hand getHand() {
        return hand;
    }

    public void draw(Deck deck){
        Card card = deck.draw();
        if(card != null) {
            hand.add(card);
        } else {
            System.out.println("No more cards in the deck.");
        }
    }

    // 出牌邏輯由子類實作（Human/AIPlayer、各遊戲規則不同)
    public abstract Card takeTurn(List<Card> playableCards);

    @Override
    public String toString() {
        return name;
    }
}
