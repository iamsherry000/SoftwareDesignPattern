package ch3_complexBV.Gym5_RPG.src;

import java.util.ArrayList;
import java.util.List;

public class Troop {
    private final int id;
    private final List<Role> roles = new ArrayList<>();

    public Troop(int id) {
        this.id = id;
    }

    public int getId() { return id; }

    public void addRole(Role role) {
        role.setTroop(this);
        roles.add(role);
    }

    public List<Role> getRoles() {
        return roles;
    }

    public List<Role> getAliveRoles() {
        return roles.stream().filter(Role::isAlive).toList();
    }

    public boolean isAnnihilated() {
        return getAliveRoles().isEmpty();
    }
}
