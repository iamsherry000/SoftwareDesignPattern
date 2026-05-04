package ch3_complexBV.Gym5_RPG.src.skill;

import java.util.List;

import ch3_complexBV.Gym5_RPG.src.Role;
import ch3_complexBV.Gym5_RPG.src.observer.CurseObserver;
import ch3_complexBV.Gym5_RPG.src.observer.DeathObserver;

public class Curse extends Skill {
    public Curse() {
        super("詛咒", 100, 1);
    }

    @Override
    public void execute(Role actor, List<Role> targets) {
        actor.consumeMp(getMpCost());
        Role target = targets.get(0);
        announce(actor, targets);
        if (!alreadyCursedBy(target, actor)) {
            target.addDeathObserver(new CurseObserver(actor));
        }
    }

    private boolean alreadyCursedBy(Role target, Role caster) {
        for (DeathObserver o : target.getDeathObservers()) {
            if (o instanceof CurseObserver c && c.getCaster() == caster) return true;
        }
        return false;
    }
}
