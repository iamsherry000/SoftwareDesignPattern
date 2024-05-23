package src.tw.SherryTseng.CollisionDetector;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        World world = new World();
        Scanner scanner = new Scanner(System.in);

        world.initWorld();
        // world.printSprites(); 
        world.printSpritesLocations();

        while (true) {
            try {
                System.out.print("Enter two positions (x1 x2): ");
                int x1 = scanner.nextInt();
                int x2 = scanner.nextInt();

                // To make sure the sprite on the location
                Sprite sprite1 = world.getSpriteAt(x1);
                Sprite sprite2 = world.getSpriteAt(x2);

                System.out.println((sprite1 != null ? sprite1 : "Choose another one"));
                System.out.println((sprite2 != null ? sprite2 : "None"));

                // world.move(x1, x2);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }


    }
}
