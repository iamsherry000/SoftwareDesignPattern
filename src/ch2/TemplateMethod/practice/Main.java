package TemplateMethod.practice;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Game game = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Welcome Welcome ===");
        System.out.println("How many human players? (0-4)");
        int humanCount = scanner.nextInt();
        scanner.nextLine(); // 吃掉換行
        System.out.println("Now choose! 1. ShowDown Game 2. Uno Game");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if(choice == 1) {
            game = new ShowDownGame(humanCount);
        }
        else if(choice == 2) {
            game = new UnoGame(humanCount);
        } else {
            System.out.println("Invalid choice. Exiting.");
            return;
        }
        game.startGame();
        System.out.println("=== Game Over! ===");
    }
}
