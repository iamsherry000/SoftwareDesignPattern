package ch3_complexBV.StatePattern.v1.states;

import ch3_complexBV.StatePattern.v1.Direction;
import ch3_complexBV.StatePattern.v1.Role;
import ch3_complexBV.StatePattern.v1.RoleState;
import java.util.Set;

/**
 * Stockpile State – lasts 2 turns, then transitions to Erupting.
 * If attacked during stockpile, reverts to Normal immediately (no Invincible granted).
 */
public class StockpileState implements RoleState {
    private int turnsLeft = 2;

    @Override public String getName() { return "Stockpile"; }
    @Override public void onTurnStart(Role role) {}

    /**
     * Being attacked cancels the charge and the damage still applies.
     * preventInvincibleAfterHit() returning true ensures no Invincible is granted.
     */
    @Override
    public int onReceiveDamage(int dmg, Role role) {
        role.setState(new NormalState());
        System.out.println("  [Stockpile] Hit! Charge cancelled, state reverted to Normal.");
        return dmg;
    }

    @Override public int  getActionsPerTurn()                  { return 1; }
    @Override public Set<Direction> getAllowedMoveDirections()  { return null; }
    @Override public boolean canAttack()                       { return true; }
    @Override public boolean preventInvincibleAfterHit()       { return true; }
    @Override public boolean isFullMapAttack()                  { return false; }

    @Override
    public void onTurnEnd(Role role) {
        if (--turnsLeft <= 0) {
            role.setState(new EruptingState());
            System.out.println("  [Stockpile] Charge complete! Entering ERUPTING state!");
        }
    }
}
