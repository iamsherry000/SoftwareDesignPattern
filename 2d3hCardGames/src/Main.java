import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Which game would you like to play? (Uno/Showdown)");
        String gameChoice = scanner.nextLine();

        Game game;
        if (gameChoice.equalsIgnoreCase("Uno")) {
            game = new UnoGame();
        } else if (gameChoice.equalsIgnoreCase("Showdown")) {
            game = new ShowdownGame();
        } else {
            System.out.println("Invalid choice!");
            return;
        }

        game.start();
        
    }
}
