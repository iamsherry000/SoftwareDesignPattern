package ch3_complexBV.CommandPattern;

import ch3_complexBV.CommandPattern.Objects.*;
import ch3_complexBV.CommandPattern.commands.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Receivers
        Tank tank = new Tank();
        Telecom telecom = new Telecom();

        // Invoker
        Keyboard keyboard = new Keyboard();
        MainController controller = new MainController(keyboard);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("(1) 快捷鍵設置 (2) Undo (3) Redo (字母) 按下按鍵: ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) continue;

            if (input.equals("1")) {
                System.out.println("設置巨集指令 (y/n)：");
                String yn = sc.nextLine().trim().toLowerCase(); // 讀取 y/n
                System.out.print("Key: ");
                String keyStr = sc.nextLine().trim().toLowerCase();

                if (keyStr.length() != 1 || keyStr.charAt(0) < 'a' || keyStr.charAt(0) > 'z') {
                    System.out.println("請輸入 a-z 的單一字母作為鍵位。");
                    continue;
                }
                char key = keyStr.charAt(0);
                if(yn.equals("y")) {
                    // todo: for MacroCommands y
                    // setMacroCommands();
                }
                else if(yn.equals("n")) {
                    System.out.println("要將哪一道指令設置到快捷鍵 " + keyStr + "上:");
                    printMenuOptions();
                    String cmdInput = sc.nextLine().trim();
                    contrller.setCommand(key, cmdInput);
                }
                else {
                    System.out.println("請輸入 y 或 n。");
                }
            } else if (input.equals("2")) {
                System.out.println("Undo");
                //todo: controller.undo();
            } else if (input.equals("3")) {
                System.out.println("Redo");
                //todo: controller.redo();
            } else if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
                // 按鍵
                char key = Character.toLowerCase(input.charAt(0));
                //todo: controller.pressKey(key);
            } else {
                // 其他輸入：結束或忽略
                // 可做成 q 離開
                if (input.equalsIgnoreCase("q")) break;
                System.out.println("指令不合法。");
            }
        }
    }

    private static void printMenuOptions() {
        System.out.println("(0) MoveTankForward");
        System.out.println("(1) MoveTankBackward");
        System.out.println("(2) ConnectTelecom");
        System.out.println("(3) DisconnectTelecom");
        System.out.println("(4) ResetMainControlKeyboard");
    }


}
