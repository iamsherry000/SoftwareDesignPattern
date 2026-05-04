package ch3_complexBV.Gym5_RPG.src.skill;

import java.util.ArrayList;
import java.util.List;

import ch3_complexBV.Gym5_RPG.src.Role;
import ch3_complexBV.Gym5_RPG.src.Troop;

public class SelfExplosion extends Skill {
    private static final int BASE_DAMAGE = 150;

    public SelfExplosion() {
        super("自爆", 200, -1);
    }

    @Override
    public List<Role> targetPool(Role actor, Troop ally, Troop enemy) {
        List<Role> all = new ArrayList<>();
        all.addAll(ally.getAliveRoles());
        all.addAll(enemy.getAliveRoles());
        all.remove(actor);
        return all;
    }

    @Override
    public void execute(Role actor, List<Role> targets) {
        actor.consumeMp(getMpCost());
        announce(actor, targets);
        int damage = BASE_DAMAGE + actor.getState().bonusDamagePerVictim();
        for (Role t : targets) dealDamage(actor, t, damage);
        actor.receiveDamage(actor.getHp());   // 自殺，印出死亡訊息
    }
}
