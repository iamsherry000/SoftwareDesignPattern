package src.tw.SherryTseng.CardGames;

public class Game {
    

    public static void start(){
        playerNameHimself();
    }

    public static void playerNameHimself() {
        HumanPlayer p1 = new HumanPlayer();
        String p1Name = p1.nameHimself();
        System.out.println(p1Name);
    }
}
