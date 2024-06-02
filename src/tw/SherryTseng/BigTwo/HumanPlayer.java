package src.tw.SherryTseng.BigTwo;

import java.util.Scanner;

public class HumanPlayer extends Player{
    private Scanner scanner;

    public HumanPlayer() {
        super(""); 
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void nameHimself() {
        System.out.println("Please enter your name: ");
        this.name = scanner.nextLine();
    }
}
