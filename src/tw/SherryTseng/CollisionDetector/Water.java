package src.tw.SherryTseng.CollisionDetector;

public class Water extends Sprite {
    public Water(int location) {
        super(location);
    }

    @Override
    public String toString() {
        return "Water at " + location;
    }
}
