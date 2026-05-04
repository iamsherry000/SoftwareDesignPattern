package ch3_complexBV.Gym5_RPG.src.skill.punch;

import ch3_complexBV.Gym5_RPG.src.Role;
import ch3_complexBV.Gym5_RPG.src.skill.Skill;
import ch3_complexBV.Gym5_RPG.src.state.CheerupState;
import ch3_complexBV.Gym5_RPG.src.state.NormalState;

public class CheerupHandler extends PunchHandler {
    private static final int DAMAGE = 100;

    @Override
    protected boolean applies(Role target) {
        return target.getState() instanceof CheerupState;
    }

    @Override
    protected void apply(Role actor, Role target) {
        int damage = DAMAGE + actor.getState().bonusDamagePerVictim();
        Skill.dealDamage(actor, target, damage);
        target.setState(new NormalState());
    }
}
