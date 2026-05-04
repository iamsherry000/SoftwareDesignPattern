package ch3_complexBV.Gym5_RPG.src.skill;

import java.util.List;

import ch3_complexBV.Gym5_RPG.src.Role;
import ch3_complexBV.Gym5_RPG.src.Troop;

public class SelfHealing extends Skill {
    private static final int HEAL_AMOUNT = 150;

    public SelfHealing() {
        super("自我治療", 50, 1);
    }

    @Override
    public List<Role> targetPool(Role actor, Troop ally, Troop enemy) {
        return List.of(actor);
    }

    @Override
    public void execute(Role actor, List<Role> targets) {
        actor.consumeMp(getMpCost());
        System.out.println(actor.display() + " 使用了 " + getName() + "。");
        actor.heal(HEAL_AMOUNT);
    }
}
