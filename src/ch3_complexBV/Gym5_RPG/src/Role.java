package ch3_complexBV.Gym5_RPG.src;

import java.util.ArrayList;
import java.util.List;

import ch3_complexBV.Gym5_RPG.src.observer.DeathObserver;
import ch3_complexBV.Gym5_RPG.src.skill.Skill;
import ch3_complexBV.Gym5_RPG.src.state.NormalState;
import ch3_complexBV.Gym5_RPG.src.state.State;

public class Role {
    private final String name;
    private int hp;
    private int mp;
    private final int strength;
    private State state;
    private Troop troop;
    private final List<Skill> skills = new ArrayList<>();
    private final List<DeathObserver> deathObservers = new ArrayList<>();

    public Role(String name, int hp, int mp, int strength) {
        this.name = name;
        this.hp = hp;
        this.mp = mp;
        this.strength = strength;
        this.state = new NormalState();
    }

    public List<Skill> getSkills()      { return skills; }
    public void addSkill(Skill skill)   { skills.add(skill); }

    public String getName()             { return name; }
    public int getHp()                  { return hp; }
    public int getMp()                  { return mp; }
    public int getStrength()            { return strength; }
    public State getState()             { return state; }
    public Troop getTroop()             { return troop; }
    public int getTroopId()             { return troop.getId(); }

    public void setState(State state)   { this.state = state; }
    public void setTroop(Troop troop)   { this.troop = troop; }

    public void addDeathObserver(DeathObserver o) { deathObservers.add(o); }
    public List<DeathObserver> getDeathObservers() { return deathObservers; }

    public boolean isAlive() { return hp > 0; }

    public String display() {
        return "[" + getTroopId() + "]" + name;
    }

    public void receiveDamage(int damage) {
        if (hp <= 0) return;
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            System.out.println(display() + " 死亡。");
            for (DeathObserver o : deathObservers) o.onDeath(this);
        }
    }

    public void consumeMp(int amount) {
        mp -= amount;
        if (mp < 0) mp = 0;
    }

    public void heal(int amount) {
        hp += amount;
    }
}
