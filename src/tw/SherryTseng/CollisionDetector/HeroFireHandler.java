package src.tw.SherryTseng.CollisionDetector;

public class HeroFireHandler extends CollisionHandler {
    public HeroFireHandler(World world) {
        super(world);
    }

    @Override
    public void handleCollision(Sprite sprite1, Sprite sprite2) {
        if ((sprite1 instanceof Hero && sprite2 instanceof Fire) || (sprite1 instanceof Fire && sprite2 instanceof Hero)) {
            Hero hero = sprite1 instanceof Hero ? (Hero) sprite1 : (Hero) sprite2;
            Sprite fire = sprite1 instanceof Fire ? sprite1 : sprite2;

            System.out.println("Hero and Fire collide, Hero loses 10 HP, Fire removed");
            hero.decreaseHP(10);
            world.removeSprite(fire);
            if(hero.getHP() == 0) {
                world.removeSprite(hero);
            }

            if (hero.getHP() > 0 && sprite1 instanceof Hero) {
                sprite1.setLocation(sprite2.getLocation());
            }
        } else {
            super.handleCollision(sprite1, sprite2);
        }
    }
}
