package ch3_complexBV.Gym5_RPG.src.observer;

import ch3_complexBV.Gym5_RPG.src.Role;

public interface DeathObserver {
    void onDeath(Role dead);
}
