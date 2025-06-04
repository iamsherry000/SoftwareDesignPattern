package StrategyPattern;

import static java.lang.Math.max;

public class Hero {
    private int hp = 100;
    private final String name;
    private final AttackType attackType;

    public Hero(String name, AttackType attackType) {
        this.name = name;
        this.attackType = attackType;
    }

    public String getName() {
        return name;
    }

    public void attack(Hero hero) {
        attackType.attack(this, hero);
    }

    public void damage(int hpLost) {
        if (isDead()) {return;}
        setHp(getHp() - hpLost);
        System.out.printf("Hero %s got %d damage，hp now %d。\n", name, hpLost, hp);
    }

    public boolean isDead() {
        return hp <= 0;
    }

    private void setHp(int hp) {
        this.hp = max(0, hp);
    }

    public int getHp() {
        return hp;
    }
}
