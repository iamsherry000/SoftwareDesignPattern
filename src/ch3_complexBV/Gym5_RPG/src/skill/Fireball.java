package ch3_complexBV.Gym5_RPG.src.skill;

import java.util.List;

import ch3_complexBV.Gym5_RPG.src.Role;

public class Fireball extends Skill {
    private static final int BASE_DAMAGE = 50;

    public Fireball() {
        super("火球", 50, -1);   // -1 means "all from target pool"
    }

    @Override
    public void execute(Role actor, List<Role> targets) {
        actor.consumeMp(getMpCost());
        announce(actor, targets);
        int damage = BASE_DAMAGE + actor.getState().bonusDamagePerVictim();
        for (Role t : targets) dealDamage(actor, t, damage);
    }
}
