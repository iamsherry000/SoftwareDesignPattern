package ch3_complexBV.Gym5_RPG.src.skill.punch;

import ch3_complexBV.Gym5_RPG.src.Role;
import ch3_complexBV.Gym5_RPG.src.skill.Skill;

public class HighHpHandler extends PunchHandler {
    private static final int HP_THRESHOLD = 500;
    private static final int DAMAGE = 300;

    @Override
    protected boolean applies(Role target) {
        return target.getHp() >= HP_THRESHOLD;
    }

    @Override
    protected void apply(Role actor, Role target) {
        int damage = DAMAGE + actor.getState().bonusDamagePerVictim();
        Skill.dealDamage(actor, target, damage);
    }
}
