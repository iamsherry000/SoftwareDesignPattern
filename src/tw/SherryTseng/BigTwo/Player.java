package src.tw.SherryTseng.BigTwo;

import java.util.List;
import java.util.Scanner;

public abstract class Player {
    protected String name;
    protected Hand hand;
    private int winRound;

    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
        this.winRound = 0;
    }

    abstract void nameHimself();

    public String getName() { 
        return name;
    }

    public Hand getHand() {
        return hand;
    }
}
