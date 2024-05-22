package game.uno;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class TurnMove {
    private final Player player;
    private final boolean pass;
    private final Card card;

    private TurnMove(Player player, boolean pass, Card card) {
        this.player = player;
        this.pass = pass;
        this.card = card;
    }

    public static TurnMove pass(Player player) {
        return new TurnMove( player, true, null);
    }

    public static TurnMove play(Player player, Card card) {
        return new TurnMove(player, false, card);
    }

    public Card getCard() {
        return card;
    }

    public boolean isPass() {
        return pass;
    }

    public Player getPlayer() {
        return player;
    }

    public void undo() {
        player.addHandCard(card);
    }
}
