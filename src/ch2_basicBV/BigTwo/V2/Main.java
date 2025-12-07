
import card.Card;
import card.Rank;
import card.Suit;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Card club3 = new Card(Suit.CLUB, Rank.THREE);
        Player[] players = new Player[4];
        int round = 1; 
        Scanner scanner = new Scanner(System.in);

        // 玩家命名初始化
        for(int i = 0; i < 4; i++) {
            players[i] = playerNaming(scanner);
        }

        // Deck 洗牌、發牌

        
        // 第一回合開始
        // if (round == 1) {
        //     System.out.println("第 1 回合開始");
        //     // 找到第一位玩家 = 擁有梅花3的玩家
        //     int firstPlayerIndex = findFirstPlayer(players);
        //     System.out.println("擁有梅花3的第一位玩家是：" + players[firstPlayerIndex].getName());

        // } else {
        //     // todo 
        // }
    }

    public static Player playerNaming(Scanner scanner) {
        System.out.println("Input player's name：");
        String name = scanner.nextLine();
        return new Player(name);
    }

    // private Player findFirstPlayer(Player[] players) {
    //     Player firstPlayer;

    //     return firstPlayer;
    // } 

}