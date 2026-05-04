package ch3_complexBV.Gym5_RPG.src.skill;

import java.util.List;

import ch3_complexBV.Gym5_RPG.src.Role;

public class Waterball extends Skill {
    private static final int BASE_DAMAGE = 120;

    public Waterball() {
        super("水球", 50, 1);
    }

    @Override
    public void execute(Role actor, List<Role> targets) {
        actor.consumeMp(getMpCost());
        announce(actor, targets);
        int damage = BASE_DAMAGE + actor.getState().bonusDamagePerVictim();
        dealDamage(actor, targets.get(0), damage);
    }
}
