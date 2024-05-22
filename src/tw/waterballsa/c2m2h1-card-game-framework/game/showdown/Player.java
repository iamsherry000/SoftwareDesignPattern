package game.showdown;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public abstract class Player extends game.base.Player<Card> {
    protected int point;
    protected Showdown showdown;

    protected abstract Card takeTurn();

    public void setShowdown(Showdown showdown) {
        this.showdown = showdown;
    }

    public void gainPoint() {
        point++;
    }

    public int getPoint() {
        return point;
    }
}