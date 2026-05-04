package ch3_complexBV.Gym5_RPG.src.skill;

import java.util.List;

import ch3_complexBV.Gym5_RPG.src.Role;
import ch3_complexBV.Gym5_RPG.src.state.PoisonedState;

public class Poison extends Skill {
    public Poison() {
        super("下毒", 80, 1);
    }

    @Override
    public void execute(Role actor, List<Role> targets) {
        Role target = targets.get(0);
        actor.consumeMp(getMpCost());
        announce(actor, targets);
        target.setState(new PoisonedState());
    }
}
