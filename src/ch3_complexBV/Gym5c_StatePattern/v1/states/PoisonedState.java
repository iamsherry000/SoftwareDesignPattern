package ch3_complexBV.StatePattern.v1.states;

import ch3_complexBV.StatePattern.v1.Direction;
import ch3_complexBV.StatePattern.v1.Role;
import ch3_complexBV.StatePattern.v1.RoleState;
import java.util.Set;

/**
 * Poisoned State – lasts 3 turns.
 * Role loses 15 HP at the start of each turn.
 */
public class PoisonedState implements RoleState {
    private int turnsLeft = 3;

    @Override public String getName() { return "Poisoned"; }

    /** Deduct 15 HP directly (bypasses state.onReceiveDamage, this is env damage). */
    @Override public void onTurnStart(Role role) {
        role.takeDamage(15);
        System.out.println("  [Poisoned] -15 HP (remaining: " + role.getHp() + "/" + role.getMaxHp() + ")");
    }

    @Override public int  onReceiveDamage(int dmg, Role role)  { return dmg; }
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
