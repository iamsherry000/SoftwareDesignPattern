package abstractClass;

public abstract class Player {
    private final int number; // final no setter
    public abstract Decision decide();

    public Player(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
