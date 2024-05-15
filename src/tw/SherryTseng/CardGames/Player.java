package src.tw.SherryTseng.CardGames;

public abstract class Player {
    protected String name;

    // Constructor
    public Player(String name) {
        this.name = name;
    }

    // getter
    public String getName() { 
        return name;
    }

    public abstract void nameHimself();
}
