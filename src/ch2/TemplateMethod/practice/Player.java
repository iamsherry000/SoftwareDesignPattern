package TemplateMethod.practice;

import java.util.Scanner;

public class Player {
    String name;
    // Hand hand;

    public Player() {
        setName();
    }

    private String getName() {
        return name;
    }

    private void setName() {
        this.name = name;
    }

    public void setHumanNumber() {
        System.out.println("Please input your human number: ");
        Scanner scanner = new Scanner(System.in);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input, please enter an integer.");
            scanner.next(); // skip invalid
        }
        int number = scanner.nextInt();
        // createHumanPlayer(number);
    }
}
