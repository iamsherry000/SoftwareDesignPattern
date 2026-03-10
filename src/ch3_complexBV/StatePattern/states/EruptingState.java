package ch3_complexBV.StatePattern.states;

import ch3_complexBV.StatePattern.Direction;
import ch3_complexBV.StatePattern.Role;
import ch3_complexBV.StatePattern.RoleState;
import java.util.Set;

/**
 * Erupting State – lasts 3 turns, then transitions to Teleport.
 * isFullMapAttack() returns true, so Character.attack() hits ALL monsters
 * on the entire map for 50 damage each — without modifying Character's code.
 */
public class EruptingState implements RoleState {
    private int turnsLeft = 3;

    @Override public String getName() { return "Erupting"; }
    @Override public void onTurnStart(Role role) {
        System.out.println("  [Erupting] FULL-MAP ATTACK mode active!");
    }
    @Override public int  onReceiveDamage(int dmg, Role role)  { return dmg; }
    @Override public int  getActionsPerTurn()                  { return 1; }
    @Override public Set<Direction> getAllowedMoveDirections()  { return null; }
    @Override public boolean canAttack()                       { return true; }
    @Override public boolean preventInvincibleAfterHit()       { return false; }

    /** Key hook: Character queries this before deciding how to resolve attack. */
    @Override public boolean isFullMapAttack()                  { return true; }

    @Override
    public void onTurnEnd(Role role) {
        if (--turnsLeft <= 0) {
            role.setState(new TeleportState());
            System.out.println("  [Erupting] Eruption over! Entering TELEPORT state.");
        }
    }
}
