package ch3_complexBV.Gym5_RPG.src.skill;

import java.util.List;
import java.util.stream.Collectors;

import ch3_complexBV.Gym5_RPG.src.Role;
import ch3_complexBV.Gym5_RPG.src.Troop;

public abstract class Skill {
    private final String name;
    private final int mpCost;
    private final int targetAmount;

    protected Skill(String name, int mpCost, int targetAmount) {
        this.name = name;
        this.mpCost = mpCost;
        this.targetAmount = targetAmount;
    }

    public String getName()      { return name; }
    public int getMpCost()       { return mpCost; }
    public int getTargetAmount() { return targetAmount; }

    public abstract void execute(Role actor, List<Role> targets);

    public List<Role> targetPool(Role actor, Troop ally, Troop enemy) {
        return enemy.getAliveRoles();
    }

    protected void announce(Role actor, List<Role> targets) {
        if (targets.isEmpty()) {
            System.out.println(actor.display() + " 使用了 " + getName() + "。");
            return;
        }
        String joined = targets.stream().map(Role::display).collect(Collectors.joining(", "));
        System.out.println(actor.display() + " 對 " + joined + " 使用了 " + getName() + "。");
    }

    public static void dealDamage(Role actor, Role target, int damage) {
        System.out.println(actor.display() + " 對 " + target.display()
                           + " 造成 " + damage + " 點傷害。");
        target.receiveDamage(damage);
    }
}
