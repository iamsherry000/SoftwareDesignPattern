package ch3_complexBV.CommandPattern;

import ch3_complexBV.CommandPattern.Objects.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Tank tank = new Tank();
        Telecom telecom = new Telecom();
        Keyboard keyboard = new Keyboard();
        MainController controller = new MainController();

        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("(1) 快捷鍵設置 (2) Undo (3) Redo (字母) 按下按鍵: ");
            int input = in.nextInt();
            if (1 == input){
                System.out.println("設置巨集指令 (y/n)：");
                char c = in.next().charAt(0); // 讀取 y/n
            } else if (2 == input) {
                controller.undo();
            } else if (3 == input) {
                controller.redo();
            } else {
                controller.press(input);
            }
        }
    }
}
