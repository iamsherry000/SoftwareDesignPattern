package ch2_basicBV.BigTwo.V3;

import ch2_basicBV.BigTwo.V3.card.Deck;
import ch2_basicBV.BigTwo.V3.game.Player;
import ch2_basicBV.BigTwo.V3.patterns.CardPatternHandler;

public class BigTwo {
    private final CardPatternHandler handlerChain;
    private Player[] players;
    private final Deck deck;

    public BigTwo(CardPatternHandler handlerChain) {
        this.handlerChain = handlerChain;
        this.deck = new Deck();
    }

    public void setPlayers(Player[] players){
        this.players = players;
    }

    public void start() {
        System.out.println("BigTwo started");
        deck.shuffle();
        
    }
}
