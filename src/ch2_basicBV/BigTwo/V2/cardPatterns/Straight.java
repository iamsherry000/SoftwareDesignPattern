package cardPatterns;

import card.Card;
import java.util.List;

public class Straight extends CardPattern {

    public Straight(List<Card> cards) {
        super(cards);
    }

    private boolean isStraight() {
        if(this.getCards().size() != 5) {
            return false;
        }
        for(int i = 0; i < this.getCards().size() - 1; i++) {
            if(this.getCards().get(i).getRank().getValue() + 1 != this.getCards().get(i + 1).getRank().getValue()) {
                return false;
            }
        }
        return true;
    }
}