package ch3_complexBV.Gym5_RPG.src.skill;

import java.util.List;

import ch3_complexBV.Gym5_RPG.src.Role;
import ch3_complexBV.Gym5_RPG.src.observer.SlimeObserver;

public class Summon extends Skill {
    private static final int SLIME_HP = 100;
    private static final int SLIME_MP = 0;
    private static final int SLIME_STR = 50;

    public Summon() {
        super("召喚", 150, 0);
    }

    @Override
    public void execute(Role actor, List<Role> targets) {
        actor.consumeMp(getMpCost());
        System.out.println(actor.display() + " 使用了 " + getName() + "。");

        Role slime = new Role("Slime", SLIME_HP, SLIME_MP, SLIME_STR);
        slime.addSkill(new BasicAttack());
        slime.addDeathObserver(new SlimeObserver(actor));
        actor.getTroop().addRole(slime);
    }
}
