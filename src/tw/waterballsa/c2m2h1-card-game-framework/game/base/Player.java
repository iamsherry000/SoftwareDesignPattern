package game.base;


/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public abstract class Player<Card> {
    protected Hand<Card> hand = new Hand<>();
    protected String name;

    public abstract void nameSelf(int order);

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addHandCard(Card card) {
        this.hand.addCard(card);
    }

    public Hand<Card> getHand() {
        return hand;
    }
}
