package src.tw.SherryTseng.CollisionDetector;

public abstract class CollisionHandler {
    protected CollisionHandler nextHandler;
    protected World world;

    public CollisionHandler(World world) {
        this.world = world;
    }

    public void setNextHandler(CollisionHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public void handleCollision(Sprite sprite1, Sprite sprite2) {
        if (nextHandler != null) {
            nextHandler.handleCollision(sprite1, sprite2);
        }
    }
}
