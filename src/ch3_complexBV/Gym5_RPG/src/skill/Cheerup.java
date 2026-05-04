package ch3_complexBV.Gym5_RPG.src.skill;

import java.util.List;

import ch3_complexBV.Gym5_RPG.src.Role;
import ch3_complexBV.Gym5_RPG.src.Troop;
import ch3_complexBV.Gym5_RPG.src.state.CheerupState;

public class Cheerup extends Skill {
    public Cheerup() {
        super("鼓舞", 100, 3);
    }

    @Override
    public List<Role> targetPool(Role actor, Troop ally, Troop enemy) {
        return ally.getAliveRoles().stream()
            .filter(r -> r != actor)
            .toList();
    }

    @Override
    public void execute(Role actor, List<Role> targets) {
        actor.consumeMp(getMpCost());
        announce(actor, targets);
        for (Role t : targets) t.setState(new CheerupState());
    }
}
