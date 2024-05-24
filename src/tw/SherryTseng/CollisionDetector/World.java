package src.tw.SherryTseng.CollisionDetector;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class World {
    private List<Sprite> sprites;
    private int SPRITENUM = 10;
    private static final int WORLDSIZE = 30;
    private CollisionHandler COR_handler;

    public World() {
        sprites = new ArrayList<>();
        initWorld();
        initCollisionHandlers();
    }

    void initWorld() {
        Random rand = new Random();
        Set<Integer> usedLocations = new HashSet<>();

        // 會重複 location 
        // for (int i = 0 ; i < SPRITENUM; i++) {}

        while (sprites.size() < SPRITENUM) {
            int location = rand.nextInt(WORLDSIZE);
            if (usedLocations.contains(location)) {
                continue; // Skip this iteration if location is already used
            }
            usedLocations.add(location);
            int type = rand.nextInt(3);

            switch (type) {
                case 0:
                    sprites.add(new Hero(location));
                    break;
                case 1:
                    sprites.add(new Water(location));
                    break;
                case 2:
                    sprites.add(new Fire(location));
                    break;
            }
        }
    }

    void initCollisionHandlers() {
        CollisionHandler sameHandler = new SameHandler(this);
        CollisionHandler fireWaterHandler = new FireWaterHandler(this);

        sameHandler.setNextHandler(fireWaterHandler);
        COR_handler = sameHandler;
    }

    public void printSprites() {
        for (Sprite sprite : sprites) {
            System.out.println(sprite);
        }
    }

    public void printSpritesLocations() {
        List<Integer> locations = new ArrayList<>();
        for (Sprite sprite : sprites) {
            locations.add(sprite.location);
        }
        System.out.println(locations);
    }

    public Sprite getSpriteAt(int location) {
        for (Sprite sprite : sprites) {
            if (sprite.getLocation() == location) {
                return sprite;
            }
        }
        return null;
    }

    public void removeSprite(Sprite sprite) {
        sprites.remove(sprite);
        //world.printSprites();
    }

    // public void move(int x1, int x2) {
    //     Sprite sprite1 = getSpriteAt(x1);
    //     Sprite sprite2 = getSpriteAt(x2);
    //     if (sprite1 != null && sprite2 != null) {
    //         handleCollision(sprite1, sprite2);
    //     } else if (sprite1 != null) {
    //         sprite1.location = x2;
    //     }
    // }

    public void move(int x1, int x2) {
        Sprite sprite1 = getSpriteAt(x1);
        Sprite sprite2 = getSpriteAt(x2);

        if (sprite1 != null && sprite2 != null) {
            COR_handler.handleCollision(sprite1, sprite2);
        } else if (sprite1 != null) {
            sprite1.location = x2;
        }
    }
    
}
