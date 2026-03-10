package ch3_complexBV.StatePattern.states;

import ch3_complexBV.StatePattern.Direction;
import ch3_complexBV.StatePattern.Role;
import ch3_complexBV.StatePattern.RoleState;
import java.util.EnumSet;
import java.util.Set;

/**
 * Orderless (Chaos) State – lasts 3 turns.
 * At the start of each turn a coin flip decides:
 *   Heads → only UP / DOWN movement allowed.
 *   Tails → only LEFT / RIGHT movement allowed.
 * The role may NOT attack while Orderless.
 */
public class OrderlessState implements RoleState {
    private int turnsLeft = 3;
    private Set<Direction> currentAllowed = EnumSet.of(Direction.UP, Direction.DOWN);

    @Override public String getName() { return "Orderless"; }

    @Override
    public void onTurnStart(Role role) {
        if (Math.random() < 0.5) {
            currentAllowed = EnumSet.of(Direction.UP, Direction.DOWN);
            System.out.println("  [Orderless] This turn: can only move UP or DOWN.");
        } else {
            currentAllowed = EnumSet.of(Direction.LEFT, Direction.RIGHT);
            System.out.println("  [Orderless] This turn: can only move LEFT or RIGHT.");
        }
    }

    @Override public int  onReceiveDamage(int dmg, Role role)  { return dmg; }
    @Override public int  getActionsPerTurn()                  { return 1; }
    @Override public Set<Direction> getAllowedMoveDirections()  { return currentAllowed; }
    @Override public boolean canAttack()                       { return false; }
    @Override public boolean preventInvincibleAfterHit()       { return false; }
    @Override public boolean isFullMapAttack()                  { return false; }

    @Override
    public void onTurnEnd(Role role) {
        if (--turnsLeft <= 0) role.setState(new NormalState());
    }
}
