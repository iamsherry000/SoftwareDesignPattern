package ch3_complexBV.Gym5_RPG.src.skill;

import java.util.List;

import ch3_complexBV.Gym5_RPG.src.Role;
import ch3_complexBV.Gym5_RPG.src.state.PetrochemicalState;

public class Petrochemical extends Skill {
    public Petrochemical() {
        super("石化", 100, 1);
    }

    @Override
    public void execute(Role actor, List<Role> targets) {
        Role target = targets.get(0);
        actor.consumeMp(getMpCost());
        announce(actor, targets);
        target.setState(new PetrochemicalState());
    }
}
