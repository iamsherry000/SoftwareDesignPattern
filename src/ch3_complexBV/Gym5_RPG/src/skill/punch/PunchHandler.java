package ch3_complexBV.Gym5_RPG.src.skill.punch;

import ch3_complexBV.Gym5_RPG.src.Role;

public abstract class PunchHandler {
    private PunchHandler next;

    public PunchHandler setNext(PunchHandler next) {
        this.next = next;
        return next;
    }

    public final void handle(Role actor, Role target) {
        if (applies(target)) {
            apply(actor, target);
        } else if (next != null) {
            next.handle(actor, target);
        }
    }

    protected abstract boolean applies(Role target);
    protected abstract void apply(Role actor, Role target);
}
