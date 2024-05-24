package src.tw.SherryTseng.CollisionDetector;

public class SameHandler extends CollisionHandler{
    public SameHandler(World world) {
        super(world);
    }

    @Override
    public void handleCollision(Sprite sprite1, Sprite sprite2) {
        if (sprite1.getClass() == sprite2.getClass()) {
            System.out.println("Same sprite, cannot move");
        } else {
            super.handleCollision(sprite1, sprite2);
        }
    }
}
