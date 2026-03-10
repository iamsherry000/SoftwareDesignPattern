package ch3_complexBV.StatePattern.states;

import ch3_complexBV.StatePattern.Direction;
import ch3_complexBV.StatePattern.Role;
import ch3_complexBV.StatePattern.RoleState;
import java.util.Set;

/**
 * Accelerated State – lasts 3 turns.
 * Role gets 2 actions per turn.
 * If attacked, immediately reverts to Normal (no Invincible granted either).
 */
public class AcceleratedState implements RoleState {
    private int turnsLeft = 3;

    @Override public String getName() { return "Accelerated"; }
    @Override public void onTurnStart(Role role) {}

    /**
     * Being attacked breaks the acceleration and the damage still applies.
     * preventInvincibleAfterHit() returning true ensures Character does NOT
     * additionally gain Invincible.
     */
    @Override
    public int onReceiveDamage(int dmg, Role role) {
        role.setState(new NormalState());
        System.out.println("  [Accelerated] Hit! State reverted to Normal.");
        return dmg;
    }

    @Override public int  getActionsPerTurn()                  { return 2; }
    @Override public Set<Direction> getAllowedMoveDirections()  { return null; }
    @Override public boolean canAttack()                       { return true; }
    @Override public boolean preventInvincibleAfterHit()       { return true; }
    @Override public boolean isFullMapAttack()                  { return false; }

    @Override
    public void onTurnEnd(Role role) {
        if (--turnsLeft <= 0) role.setState(new NormalState());
    }
}
