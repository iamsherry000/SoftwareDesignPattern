package ch3_complexBV.StatePattern.treasures;

import ch3_complexBV.StatePattern.Role;
import ch3_complexBV.StatePattern.Treasure;
import ch3_complexBV.StatePattern.states.StockpileState;

public class KingRock extends Treasure {
    public static final float TREASURE_GENERATION_RATE = 0.1f;

    public KingRock() { super(); }

    @Override public float getGenerationRate() { return TREASURE_GENERATION_RATE; }

    @Override
    public void onTouch(Role role) {
        role.setState(new StockpileState());
    }
}
