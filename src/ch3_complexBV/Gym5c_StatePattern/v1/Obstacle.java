package ch3_complexBV.StatePattern.v1;

/**
 * Obstacle – a static, impassable map object.
 * Roles cannot move onto or through an obstacle.
 * Character's attack line is also blocked by obstacles.
 */
public class Obstacle extends MapObj {
    private static final char OBSTACLE_SYMBOL = '□';

    public Obstacle() {
        super(OBSTACLE_SYMBOL);
    }
}
