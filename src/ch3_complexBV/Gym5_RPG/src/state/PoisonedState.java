package ch3_complexBV.Gym5_RPG.src.state;

import ch3_complexBV.Gym5_RPG.src.Role;

public class PoisonedState implements State {
    private static final int DAMAGE_PER_TURN = 30;
    private static final int DURATION = 3;

    private int remaining = DURATION;

    @Override
    public String getName() { return "中毒"; }

    @Override
    public void onTurnEffect(Role role) {
        role.receiveDamage(DAMAGE_PER_TURN);
    }

    @Override
    public State nextState() {
        remaining--;
        return remaining > 0 ? this : new NormalState();
    }
}
