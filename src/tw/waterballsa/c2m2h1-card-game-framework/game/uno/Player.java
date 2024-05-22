package game.uno;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public abstract class Player extends game.base.Player<Card> {

    public abstract TurnMove takeTurn(Card topCard);

}
