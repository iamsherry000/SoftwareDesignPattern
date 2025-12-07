import card.Card;
import java.util.List;

public class Player {
    
    private String name;
    private Hand hand;

    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
    }

    // 委派給 Hand 處理卡牌操作
    public void addCard(Card card) {
        hand.addCard(card);
    }

    public void removeCard(Card card) {
        hand.removeCard(card);
    }

    public List<Card> getHandCards() {
        return hand.getCards();
    }

    public int getHandSize() {
        return hand.size();
    }

    public boolean isEmpty() {
        return hand.isEmpty();
    }

    public boolean hasCard(Card card) {
        return hand.hasCard(card);
    }

    public Hand getHand() {
        return hand;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
