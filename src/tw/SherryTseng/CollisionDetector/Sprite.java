package src.tw.SherryTseng.CollisionDetector;

public class Sprite {
    protected int location;
    protected int HP;

    public Sprite(int location) {
        this.location = location;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }
}
