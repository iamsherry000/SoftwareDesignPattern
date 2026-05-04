package ch3_complexBV.Gym5_RPG.src.skill;

import java.util.List;

import ch3_complexBV.Gym5_RPG.src.Role;
import ch3_complexBV.Gym5_RPG.src.skill.punch.CheerupHandler;
import ch3_complexBV.Gym5_RPG.src.skill.punch.HighHpHandler;
import ch3_complexBV.Gym5_RPG.src.skill.punch.NormalHandler;
import ch3_complexBV.Gym5_RPG.src.skill.punch.PoisonedPetroHandler;
import ch3_complexBV.Gym5_RPG.src.skill.punch.PunchHandler;

public class OnePunch extends Skill {
    private final PunchHandler chain;

    public OnePunch() {
        super("一拳攻擊", 180, 1);
        chain = new HighHpHandler();
        chain.setNext(new PoisonedPetroHandler())
             .setNext(new CheerupHandler())
             .setNext(new NormalHandler());
    }

    @Override
    public void execute(Role actor, List<Role> targets) {
        actor.consumeMp(getMpCost());
        announce(actor, targets);
        chain.handle(actor, targets.get(0));
    }
}
