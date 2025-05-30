package abstractClass;

public class AIPlayer extends Player{

    public AIPlayer(int number) {
        super(number); // call from father Player class
    }

    @Override
    public Decision decide() {
        int randomNum = (int)(Math.random() * 3);
        switch (randomNum) {
            case 1: return Decision.ROCK;
            case 2: return Decision.PAPER;
            default: return Decision.SCISSORS;
        }
    }
}
