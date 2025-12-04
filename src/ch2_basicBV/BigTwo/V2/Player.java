import card.Card;
import java.util.List;
import java.util.ArrayList;

public class Player {
    
    private String name;
    private List<Card> handCards;

    public Player(String name) {
        this.name = name;
        this.handCards = new ArrayList<>();
    }


    public List<Card> getHandCards() {
        return new ArrayList<>(handCards); // 回傳副本以保護內部狀態
    }

    public int getHandSize() {
        return handCards.size();
    }

    public boolean isEmpty() {
        return handCards.isEmpty();
    }

    public boolean hasCard(Card card) {
        return handCards.contains(card);
    }
}
