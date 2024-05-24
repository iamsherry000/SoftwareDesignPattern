package src.tw.SherryTseng.CollisionDetector;

public class HeroWaterHandler extends CollisionHandler {
    public HeroWaterHandler(World world) {
        super(world);
    }

    @Override
    public void handleCollision(Sprite sprite1, Sprite sprite2) {
        if ((sprite1 instanceof Hero && sprite2 instanceof Water) || (sprite1 instanceof Water && sprite2 instanceof Hero)) {
            Hero hero = sprite1 instanceof Hero ? (Hero) sprite1 : (Hero) sprite2;
            Sprite water = sprite1 instanceof Water ? sprite1 : sprite2;

            System.out.println("Hero and Water collide, Hero earn 10 HP, Water removed");
            hero.increaseHP(10);
            world.removeSprite(water);

            if (sprite1 instanceof Hero) {
                sprite1.setLocation(sprite2.getLocation());
            }
        } else {
            super.handleCollision(sprite1, sprite2);
        }
    }
}
