package src.tw.SherryTseng.CollisionDetector;

public class Hero extends Sprite {
    private int hp;
    
    public Hero(int location) {
        super(location);
        this.hp = 30;
    }

    public int getHP() {
        return hp;
    }

    public void decreaseHP(int amount) {
        hp -= amount;
    }

    public void increaseHP(int amount) {
        hp += amount;
    }

    @Override
    public String toString() {
        return "Hero at " + location + " with HP:" + hp;
    }
}
