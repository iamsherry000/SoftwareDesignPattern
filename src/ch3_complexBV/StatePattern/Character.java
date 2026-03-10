package ch3_complexBV.StatePattern;

import ch3_complexBV.StatePattern.states.InvincibleState;

/**
 * Character – the player-controlled hero.
 *
 * Extends Role (which is the State Pattern Context).
 * Character's only role-specific logic:
 *   1. Its visual symbol is the arrow of its current facing Direction.
 *   2. After taking a hit it gains Invincible UNLESS the active state
 *      signals preventInvincibleAfterHit() (Stockpile / Accelerated).
 *
 * Attack behaviour is dispatched by Game based on state.isFullMapAttack():
 *   - Normal attack  → Map.attackLine(character)
 *   - Erupting attack → Map.attackAll(50)
 * Character itself never checks "which state am I in?" for this,
 * satisfying OCP when new full-map-attack states are added.
 */
public class Character extends Role {

    private static final int INITIAL_HP = 300;
    private Direction facing;

    public Character() {
        super(Direction.UP.getSymbol(), INITIAL_HP);
        // Randomise initial facing direction
        Direction[] dirs = Direction.values();
        this.facing = dirs[(int) (Math.random() * dirs.length)];
    }

    /** Symbol changes dynamically with facing direction. */
    @Override
    public char getSymbol() { return facing.getSymbol(); }

    public Direction getFacing()        { return facing; }
    public void      setFacing(Direction d) { this.facing = d; }

    /**
     * After taking damage: grant Invincible unless the state that was active
     * at the moment of the hit already handled the "no Invincible" rule
     * (Stockpile and Accelerated revert to Normal instead).
     */
    @Override
    protected void afterReceiveDamage(boolean preventInvincible) {
        if (!preventInvincible) {
            setState(new InvincibleState());
            System.out.println("  [Character] Took damage – gained Invincible!");
        }
    }
}
