package ch3_complexBV.Gym5_RPG.src.skill.punch;

import ch3_complexBV.Gym5_RPG.src.Role;
import ch3_complexBV.Gym5_RPG.src.skill.Skill;

public class NormalHandler extends PunchHandler {
    private static final int DAMAGE = 100;

    @Override
    protected boolean applies(Role target) {
        return true;   // fallback: always applies
    }

    @Override
    protected void apply(Role actor, Role target) {
        int damage = DAMAGE + actor.getState().bonusDamagePerVictim();
        Skill.dealDamage(actor, target, damage);
    }
}
