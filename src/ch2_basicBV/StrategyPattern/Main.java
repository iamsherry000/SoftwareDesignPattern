package StrategyPattern;

public class Main {
    public static void main(String[] args) {
        Hero hero1 = new Hero("Sherry1", new Earth());
        Hero hero2 = new Hero("Sherry2", new Fireball());
        Game game = new Game(hero1, hero2);
        game.start();
    }
}
