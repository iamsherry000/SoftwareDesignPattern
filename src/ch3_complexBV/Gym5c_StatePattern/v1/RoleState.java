package ch3_complexBV.StatePattern.v1;

import java.util.Set;

/**
 * State Pattern – Context: Role, State: RoleState
 *
 * Every behaviour that varies with the role's current status is declared here
 * and implemented by concrete state classes.  Role delegates these calls to its
 * current state object, so adding a brand-new state never requires touching
 * Role, Character, or Monster  (satisfying OCP).
 */
public interface RoleState {

    /** Human-readable name shown in the HUD. */
    String getName();

    /**
     * Called once at the beginning of the role's turn.
     * E.g. Poisoned deducts 15 HP, Healing adds 30 HP, Orderless randomises
     * the allowed movement directions.
     */
    void onTurnStart(Role role);

    /**
     * Filters raw incoming damage.  Returns the actual damage to apply.
     * Invincible returns 0 (no damage); AcceleratedState / StockpileState
     * revert to Normal and still let the damage through.
     */
    int onReceiveDamage(int rawDamage, Role role);

    /**
     * How many actions the role may take in one turn.
     * Accelerated = 2, all others = 1.
     */
    int getActionsPerTurn();

    /**
     * Returns the set of Directions the role is allowed to move towards.
     * {@code null} means "all four directions are allowed".
     * Orderless randomly restricts this to {UP,DOWN} or {LEFT,RIGHT}.
     */
    Set<Direction> getAllowedMoveDirections();

    /** Whether the role may choose the attack action this turn. Orderless = false. */
    boolean canAttack();

    /**
     * True for Stockpile and Accelerated: when hit, those states revert to
     * Normal so the role should NOT additionally gain Invincible.
     * Character.receiveAttack() queries this to decide whether to grant Invincible.
     */
    boolean preventInvincibleAfterHit();

    /**
     * True only for EruptingState: attack hits ALL map tiles instead of a line.
     * Character.attack() queries this so new "full-map" states never need a
     * change inside Character.
     */
    boolean isFullMapAttack();

    /**
     * Called once at the end of the role's turn.
     * Decrements the remaining-turns counter and triggers state transitions:
     * Stockpile → Erupting, Erupting → Teleport, Teleport → sets needsTeleport flag.
     */
    void onTurnEnd(Role role);
}
