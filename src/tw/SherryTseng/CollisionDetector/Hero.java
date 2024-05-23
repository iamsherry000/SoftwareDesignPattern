package src.tw.SherryTseng.CollisionDetector;

public class Hero extends Sprite {
    public Hero(int location) {
        super(location);
        this.HP = 30;
    }

    @Override
    public String toString() {
        return "Hero at " + location + " with HP:" + HP;
    }
}
