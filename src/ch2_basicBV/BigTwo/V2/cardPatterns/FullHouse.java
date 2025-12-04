public class FullHouse extends CardPattern {
    public FullHouse(List<Card> cards) {
        super(cards);
    }

    private boolean isFullHouse() {
        if(this.getCards().size() != 5) {
            return false;
        }
        for(int i = 0; i < this.getCards().size() - 1; i++) {
            if(this.getCards().get(i).getRank().getValue() != this.getCards().get(i + 1).getRank().getValue()) {
                return false;
            }
        }
        return true;
    }
}