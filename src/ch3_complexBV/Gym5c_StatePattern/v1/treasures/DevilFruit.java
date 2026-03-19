package ch3_complexBV.StatePattern.v1.treasures;

import ch3_complexBV.StatePattern.v1.Role;
import ch3_complexBV.StatePattern.v1.Treasure;
import ch3_complexBV.StatePattern.v1.states.OrderlessState;

public class DevilFruit extends Treasure {
    public static final float TREASURE_GENERATION_RATE = 0.1f;

    public DevilFruit() { super(); }

    @Override public float getGenerationRate() { return TREASURE_GENERATION_RATE; }

    @Override
    public void onTouch(Role role) {
        role.setState(new OrderlessState());
    }
}
