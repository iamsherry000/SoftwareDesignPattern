
public class Main {
    Card club3 = new Card(Suit.CLUB, Rank.THREE);
    
    public static void main(String[] args) {
        int round = 1; 

        // 玩家命名初始化
        Player[] players = new Player[4];
        for(int i = 0; i < 4; i++) {
            playerNaming(players[i]);
        }

        // Deck 洗牌、發牌

        
        // 第一回合開始
        if (round == 1) {
            System.out.println("第 1 回合開始");
            // 找到第一位玩家 = 擁有梅花3的玩家
            int firstPlayerIndex = findFirstPlayer(players);
            System.out.println("擁有梅花3的第一位玩家是：" + players[firstPlayerIndex].getName());

        } else {
            // todo 
        }
    }

    public static void playerNaming(Player player) {
        System.out.println("請輸入玩家名字：");
        Scanner scanner = new Scanner(System.in);
        player.setName(scanner.nextLine());
    }
}