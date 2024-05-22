package game.showdown;

import java.util.Random;

import static java.lang.String.format;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class AI extends Player {
    private static final Random random = new Random();

    @Override
    public void nameSelf(int order) {
        setName(format("AI-%d", order));
    }

    @Override
    protected Card takeTurn() {
        if (getHand().size() == 1) {
            return getHand().get(0);
        }
        return getHand().play(random.nextInt(getHand().size()));
    }

    public void setShowdown(Showdown showdown) {
        this.showdown = showdown;
    }
}
