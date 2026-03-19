package ch3_complexBV.StatePattern.v1.treasures;

import ch3_complexBV.StatePattern.v1.Role;
import ch3_complexBV.StatePattern.v1.Treasure;
import ch3_complexBV.StatePattern.v1.states.HealingState;

public class HealingPotion extends Treasure {
    public static final float TREASURE_GENERATION_RATE = 0.15f;

    public HealingPotion() { super(); }

    @Override public float getGenerationRate() { return TREASURE_GENERATION_RATE; }

    @Override
    public void onTouch(Role role) {
        role.setState(new HealingState());
    }
}
