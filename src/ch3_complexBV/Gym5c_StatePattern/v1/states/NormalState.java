package ch3_complexBV.StatePattern.v1.states;

import ch3_complexBV.StatePattern.v1.Direction;
import ch3_complexBV.StatePattern.v1.Role;
import ch3_complexBV.StatePattern.v1.RoleState;
import java.util.Set;

/** Normal State – the baseline; no special effects. */
public class NormalState implements RoleState {
    @Override public String getName()                           { return "Normal"; }
    @Override public void onTurnStart(Role role)               {}
    @Override public int  onReceiveDamage(int dmg, Role role)  { return dmg; }
    @Override public int  getActionsPerTurn()                  { return 1; }
    @Override public Set<Direction> getAllowedMoveDirections()  { return null; }
    @Override public boolean canAttack()                       { return true; }
    @Override public boolean preventInvincibleAfterHit()       { return false; }
    @Override public boolean isFullMapAttack()                  { return false; }
    @Override public void onTurnEnd(Role role)                  {}
}
