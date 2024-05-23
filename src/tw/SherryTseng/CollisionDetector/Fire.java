package src.tw.SherryTseng.CollisionDetector;

public class Fire extends Sprite{
    public Fire(int location) {
        super(location);
    }

    @Override
    public String toString() {
        return "Fire at " + location;
    }
}
