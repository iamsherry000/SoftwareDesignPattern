package ch3_complexBV.Gym5_RPG.src.observer;

import ch3_complexBV.Gym5_RPG.src.Role;

public class CurseObserver implements DeathObserver {
    private final Role caster;

    public CurseObserver(Role caster) {
        this.caster = caster;
    }

    public Role getCaster() {
        return caster;
    }

    @Override
    public void onDeath(Role dead) {
        if (caster.isAlive()) caster.heal(dead.getMp());
    }
}
