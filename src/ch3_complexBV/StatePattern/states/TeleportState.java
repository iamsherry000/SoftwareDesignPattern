package ch3_complexBV.StatePattern.states;

import ch3_complexBV.StatePattern.Direction;
import ch3_complexBV.StatePattern.Role;
import ch3_complexBV.StatePattern.RoleState;
import java.util.Set;

/**
 * Teleport State – lasts 1 turn.
 * At turn-end the role is flagged for teleportation (needsTeleport = true).
 * Map.teleportRole() moves the role to a random empty cell when Game sees the flag.
 */
public class TeleportState implements RoleState {
    private int turnsLeft = 1;

    @Override public String getName() { return "Teleport"; }
    @Override public void onTurnStart(Role role) {
        System.out.println("  [Teleport] Will warp to a random cell at end of this turn!");
    }
    @Override public int  onReceiveDamage(int dmg, Role role)  { return dmg; }
    @Override public int  getActionsPerTurn()                  { return 1; }
    @Override public Set<Direction> getAllowedMoveDirections()  { return null; }
    @Override public boolean canAttack()                       { return true; }
    @Override public boolean preventInvincibleAfterHit()       { return false; }
    @Override public boolean isFullMapAttack()                  { return false; }

    @Override
    public void onTurnEnd(Role role) {
        if (--turnsLeft <= 0) {
            role.setNeedsTeleport(true);
            role.setState(new NormalState());
        }
    }
}
