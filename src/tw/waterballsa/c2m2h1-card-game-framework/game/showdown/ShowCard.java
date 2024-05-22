package game.showdown;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class ShowCard implements Comparable<ShowCard> {
    private final Player player;
    private final Card card;

    public ShowCard(Player player, Card card) {
        this.player = player;
        this.card = card;
    }

    @Override
    public int compareTo(ShowCard o) {
        return card.compareTo(o.getCard());
    }

    public Player getPlayer() {
        return player;
    }

    public Card getCard() {
        return card;
    }
}
