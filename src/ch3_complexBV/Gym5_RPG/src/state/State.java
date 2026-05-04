package ch3_complexBV.Gym5_RPG.src.state;

import ch3_complexBV.Gym5_RPG.src.Role;

public interface State {
    String getName();

    default void onTurnEffect(Role role) { }

    default State nextState() { return this; }

    default boolean canAct() { return true; }

    default int bonusDamagePerVictim() { return 0; }
}
