package src.tw.SherryTseng.CardGames;

import java.util.Scanner;

public class HumanPlayer extends Player {
    
    public HumanPlayer(String name) {
        super(name); // or super(null)?
    }

    @Override
    public void nameHimself() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your name: ");
        String playerName = scanner.nextLine();
        super.name = playerName;
    }
}
