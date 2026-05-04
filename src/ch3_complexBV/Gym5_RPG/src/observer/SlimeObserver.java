package ch3_complexBV.Gym5_RPG.src.observer;

import ch3_complexBV.Gym5_RPG.src.Role;

public class SlimeObserver implements DeathObserver {
    private static final int HEAL_AMOUNT = 30;

    private final Role summoner;

    public SlimeObserver(Role summoner) {
        this.summoner = summoner;
    }

    @Override
    public void onDeath(Role dead) {
        if (summoner.isAlive()) summoner.heal(HEAL_AMOUNT);
    }
}
