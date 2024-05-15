package src.tw.SherryTseng.CardGames;

import java.util.Scanner;

public class HumanPlayer implements Player{
    
    @Override
    public String nameHimself(){ // 修正方法的返回類型為 String
        Scanner scanner = new Scanner(System.in); // 創建一個 Scanner 對象
        System.out.println("請輸入你的名字："); // 提示用戶輸入名字
        String name = scanner.nextLine(); // 讀取用戶輸入的名字
        scanner.close(); // 關閉 Scanner
        return name; // 返回用戶輸入的名字
    }
}
