package ch3_complexBV.StatePattern.states;

import ch3_complexBV.StatePattern.Direction;
import ch3_complexBV.StatePattern.Role;
import ch3_complexBV.StatePattern.RoleState;
import java.util.Set;

/**
 * Healing State – lasts up to 5 turns.
 * Role recovers 30 HP at the start of each turn.
 * Ends immediately if the role reaches full HP.
 */
public class HealingState implements RoleState {
    private int turnsLeft = 5;

    @Override public String getName() { return "Healing"; }

    @Override
    public void onTurnStart(Role role) {
        if (role.isFullHp()) {
            role.setState(new NormalState());
            System.out.println("  [Healing] Already at full HP – state ended.");
            return;
        }
        role.heal(30);
        System.out.println("  [Healing] +30 HP (now: " + role.getHp() + "/" + role.getMaxHp() + ")");
        if (role.isFullHp()) {
            role.setState(new NormalState());
            System.out.println("  [Healing] Full HP reached – state ended.");
        }
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
