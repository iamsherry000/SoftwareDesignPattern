package src.tw.SherryTseng.CollisionDetector;

public class FireWaterHandler extends CollisionHandler{
    public FireWaterHandler(World world) {
        super(world);
    }

    @Override
    public void handleCollision(Sprite sprite1, Sprite sprite2) { 
        if((sprite1 instanceof Fire && sprite2 instanceof Water) || (sprite1 instanceof Water && sprite2 instanceof Fire)) {
            System.out.println("Water and Fire collide, both removed");
            world.removeSprite(sprite1);
            world.removeSprite(sprite2);
        } else {
            super.handleCollision(sprite1, sprite2);
        }
        
    }
}
