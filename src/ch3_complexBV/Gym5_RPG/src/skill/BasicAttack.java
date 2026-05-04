package ch3_complexBV.Gym5_RPG.src.skill;

import java.util.List;

import ch3_complexBV.Gym5_RPG.src.Role;

public class BasicAttack extends Skill {
    public BasicAttack() {
        super("普通攻擊", 0, 1);
    }

    @Override
    public void execute(Role actor, List<Role> targets) {
        Role target = targets.get(0);
        int damage = actor.getStrength() + actor.getState().bonusDamagePerVictim();

        System.out.println(actor.display() + " 攻擊 " + target.display() + "。");
        System.out.println(actor.display() + " 對 " + target.display()
                           + " 造成 " + damage + " 點傷害。");
        target.receiveDamage(damage);
    }
}
