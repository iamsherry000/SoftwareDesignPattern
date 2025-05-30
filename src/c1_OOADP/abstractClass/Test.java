package abstractClass;

public class Test {
    public static void main(String[] args) {
        Game game = new Game(new HumanPlayer(1), new AIPlayer(2));
        game.start();
    }
}
