package ch3_complexBV.Gym5_RPG.src.skill.punch;

import ch3_complexBV.Gym5_RPG.src.Role;
import ch3_complexBV.Gym5_RPG.src.skill.Skill;
import ch3_complexBV.Gym5_RPG.src.state.PetrochemicalState;
import ch3_complexBV.Gym5_RPG.src.state.PoisonedState;

public class PoisonedPetroHandler extends PunchHandler {
    private static final int DAMAGE = 80;
    private static final int HITS = 3;

    @Override
    protected boolean applies(Role target) {
        return target.getState() instanceof PoisonedState
            || target.getState() instanceof PetrochemicalState;
    }

    @Override
    protected void apply(Role actor, Role target) {
        for (int i = 0; i < HITS; i++) {
            int damage = DAMAGE + actor.getState().bonusDamagePerVictim();
            Skill.dealDamage(actor, target, damage);
            if (!target.isAlive()) return;
        }
    }
}
