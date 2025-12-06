

public abstract class CardPatternHandler {
    private final CardPatternHandler next;

    protected CardPatternHandler() {
        next = null;
    }

    protected CardPatternHandler(CardPatternHandler next) {
        this.next = next;
    }

    public void setNext(CardPatternHandler next) {
        this.next = next;
    }

    public CardPatternHandler getNext() {
        return next;
    }
}