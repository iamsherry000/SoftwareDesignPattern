package ch3_complexBV.StatePattern.states;

import ch3_complexBV.StatePattern.Direction;
import ch3_complexBV.StatePattern.Role;
import ch3_complexBV.StatePattern.RoleState;
import java.util.Set;

/**
 * Invincible State – lasts 2 turns.
 * All incoming damage is absorbed (returns 0).
 */
public class InvincibleState implements RoleState {
    private int turnsLeft = 2;

    @Override public String getName() { return "Invincible"; }
    @Override public void onTurnStart(Role role) {}

    /** Absorbs all damage – the role takes no HP loss while invincible. */
    @Override public int onReceiveDamage(int dmg, Role role) { return 0; }

    @Override public int  getActionsPerTurn()                  { return 1; }
    @Override public Set<Direction> getAllowedMoveDirections()  { return null; }
    @Override public boolean canAttack()                       { return true; }
    @Override public boolean preventInvincibleAfterHit()       { return false; }
    @Override public boolean isFullMapAttack()                  { return false; }

    @Override
    public void onTurnEnd(Role role) {
        if (--turnsLeft <= 0) role.setState(new NormalState());
    }
}
