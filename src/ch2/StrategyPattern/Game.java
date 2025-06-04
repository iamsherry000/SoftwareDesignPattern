package StrategyPattern;

import java.util.List;
import static java.util.Arrays.asList;
import static java.util.Collections.swap;

public class Game {
    private final List<Hero> heroes;

    public Game(Hero hero1, Hero hero2) {
        this.heroes = asList(hero1, hero2);
    }

    public void start() {
        nextTurn();
    }

    private void nextTurn() {
        Hero attacker = heroes.get(0);
        Hero attacked = heroes.get(1);
        attacker.attack(attacked);
        swap(heroes, 0, 1);
        if (attacked.isDead()) {
            System.out.printf("Hero %s died！%s won！\n", attacked.getName(), attacker.getName());
        } else {
            nextTurn();
        }
    }


}
